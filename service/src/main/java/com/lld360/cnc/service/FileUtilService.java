package com.lld360.cnc.service;

import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileUtilService {
    Logger logger = LoggerFactory.getLogger(FileUtilService.class);
    private String fileThumbSize;

    @Autowired
    Configer configer;

    // 获取临时存放文件的目录
    public String getTempFilePath() {
        return configer.getFileBasePath() + "temp";
    }

    // 获取实际存放用户文件的根目录
    public String getUserFilePath() {
        return configer.getFileBasePath() + "user";
    }

    //上传图片
    public Map<String, String> upload(MultipartFile file) {
        String path = getTempFilePath();
        File directory = new File(path);
        if (directory.isDirectory() || directory.mkdirs()) {
            String originName = file.getOriginalFilename();

            // 后缀名
            int index = originName.lastIndexOf('.');
            String suffix = index > -1 ? originName.substring(index) : "";

            // 文件名
            String filename = new Date().getTime() + suffix;

            // 完整路径
            String fullPath = path + File.separator + filename;

            try {
                FileUtils.writeByteArrayToFile(new File(fullPath), file.getBytes());
            } catch (IOException e) {
                logger.warn("保存上传文件异常：" + e.getMessage(), e);
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10101, fullPath).setData(e.getMessage());
            }

            Map<String, String> result = new HashMap<>();
            result.put("originName", originName);
            result.put("suffix", suffix);
            result.put("fileName", filename);
            result.put("fullPath", fullPath);
            return result;
        }
        throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10102, path);
    }

    //判断文件是否是图片(不验证文件是否存在)
    public boolean isImage(File file) {
        String ext = FilenameUtils.getExtension(file.getName());
        return StringUtils.isNotEmpty(ext) && ext.toLowerCase().matches("jpg|jpeg|png|bmp|gif");
    }

    // 获取缩略图尺寸配置
    private String getThumbSizes() {
        if (fileThumbSize == null) {
            fileThumbSize = "48,72,96,144,192";
        }
        return fileThumbSize;
    }

    //裁剪图片
    public String cutFile(MultipartFile file, Rectangle region) {
        Map<String, String> result = upload(file);
        String fileName = result.get("fileName");
        try {
            File f = new File(result.get("fullPath"));
            Thumbnails.of(f).sourceRegion(region).size(200, 200).toFile(result.get("fullPath"));
        } catch (IOException e) {
            logger.warn("裁剪图片异常：" + e.getMessage(), e);
            System.out.println(e);
        }
        return fileName;
    }

    /**
     * 根据配置的size生成缩略图
     *
     * @param image 源文件
     */
    public void genThumb(File image) {
        String path = image.getParent();
        String name = image.getName();
        String ext = FilenameUtils.getExtension(name);
        String signleName = name;
        if (StringUtils.isNotEmpty(ext)) {
            signleName = name.replace("." + ext, "");
        }

        try {
            String[] sizes = getThumbSizes().split(",");
            for (String size : sizes) {
                if (size.matches("\\d+\\*\\d+")) {
                    String[] ss = size.split("\\*");
                    int w = Integer.parseInt(ss[0]);
                    int h = Integer.parseInt(ss[1]);
                    String target = String.format("%s%s%s_%d_%d.%s", path, File.separator, signleName, w, h, ext);
                    Thumbnails.of(image.getName()).size(w, h).outputQuality(1f).toFile(target);
                } else if (size.matches("\\d+")) {
                    int s = Integer.parseInt(size);
                    String target = String.format("%s%s%s_%d.%s", path, File.separator, signleName, s, ext);
                    Thumbnails.of(image).size(s, s).outputQuality(1f).toFile(target);
                }
            }
        } catch (IOException e) {
            logger.warn("生成缩略图异常：" + e.getMessage(), e);
        }
    }

    /**
     * 删除缩略图
     *
     * @param image 源文件
     */
    public void delThumb(File image) {
        String path = image.getParent();
        String name = image.getName();
        String ext = FilenameUtils.getExtension(name);
        String signleName = name;
        if (StringUtils.isNotEmpty(ext)) {
            signleName = name.replace("." + ext, "");
        }

        try {
            String[] sizes = getThumbSizes().split(",");
            String target = null;
            for (String size : sizes) {
                if (size.matches("\\d+\\*\\d+")) {
                    String[] ss = size.split("\\*");
                    int w = Integer.parseInt(ss[0]);
                    int h = Integer.parseInt(ss[1]);
                    target = String.format("%s%s%s_%d_%d.%s", path, File.separator, signleName, w, h, ext);
                } else if (size.matches("\\d+")) {
                    int s = Integer.parseInt(size);
                    target = String.format("%s%s%s_%d.%s", path, File.separator, signleName, s, ext);
                }
                if (target != null) {
                    File fthumb = new File(target);
                    if (fthumb.exists() && fthumb.isFile()) {
                        FileUtils.forceDelete(fthumb);
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("删除图片异常：" + e.getMessage(), e);
        }
    }

    /**
     * 将已上传的临时文件移动到用户正确位置
     *
     * @param fileName   临时文件名
     * @param objectType 用户类型
     * @param userId     用户ID
     * @return 文件相对路径
     */
    public String moveUploadedFile(String fileName, String objectType, Long userId) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        String path = getTempFilePath();
        File srcFile = new File(path + "/" + fileName);
        if (srcFile.exists()) {
            String targetPath = getUserFilePath() + "/" + userId;
            File targetDirectory = new File(targetPath);
            if ((targetDirectory.exists() && targetDirectory.isDirectory()) || targetDirectory.mkdirs()) {
                File targetFile = new File(targetPath + "/" + fileName);
                try {
                    FileUtils.moveFile(srcFile, targetFile);
                    if (isImage(targetFile)) {
                        genThumb(targetFile);   // 生成缩略图
                    }
                    return objectType + "/" + userId + "/" + fileName;
                } catch (IOException e) {
                    logger.warn("保存类型文件异常：" + e.getMessage(), e);
                    throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10103, srcFile, targetFile).setData(e.getMessage());
                }
            }
            throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10104, targetPath);
        }
        throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10105, srcFile);
    }

    /**
     * 删除用户文件
     *
     * @param targetFile 用户文件的相对路径
     */
    public void delUserFile(String targetFile) {
        if (StringUtils.isEmpty(targetFile)) {
            return;
        }
        File file = new File(getUserFilePath() + File.separator + targetFile);
        if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                logger.warn("删除文件异常：" + e.getMessage(), e);
            }
        }
        if (isImage(file)) {
            delThumb(file); // 删除缩略图
        }
    }

    /**
     * 将已上传的临时文件移动到用户正确位置并删除旧文件
     *
     * @param fileName   临时文件名
     * @param objectType 用户类型
     * @param userId     用户ID
     * @param oldFile    旧文件
     * @return 文件相对路径
     */
    public String moveUploadedFile(String fileName, String objectType, Long userId, String oldFile) {
        String file = moveUploadedFile(fileName, objectType, userId);
        if (StringUtils.isNotEmpty(file) && StringUtils.isNotEmpty(oldFile)) {
            delUserFile(oldFile);
        }
        return file;
    }
}
