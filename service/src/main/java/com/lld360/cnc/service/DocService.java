package com.lld360.cnc.service;

import com.artofsolving.jodconverter.DocumentConverter;
import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.DocUtils;
import com.lld360.cnc.dto.DocDto;
import com.lld360.cnc.model.*;
import com.lld360.cnc.repository.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DocService extends BaseService {

    @Autowired
    private MonthReportService monthReportService;

    @Autowired
    private DocDao docDao;

    @Autowired
    private DocTagDao docTagDao;

    @Autowired
    private DocDownloadDao docDownloadDao;

    @Autowired
    private DocImageDao docImageDao;

    @Autowired
    private UserScoreDao userScoreDao;

    @Autowired
    private UserScoreHistoryDao userScoreHistoryDao;

    @Autowired
    private SynonymService synonymService;

//    @Autowired
//    private DocumentConverter documentConverter;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    public Page<Doc> search(Map<String, Object> parameters) {
        parameters.put("state", Const.DOC_STATE_NORMAL);
        Pageable pageable = getPageable(parameters);
        long count = docDao.count(parameters);
        List<Doc> userJobFeeList = docDao.search(parameters);
        return new PageImpl<>(userJobFeeList, pageable, count);
    }

    public long count(Map<String, Object> parameters) {
        return docDao.count(parameters);
    }

    public Doc find(Long id) {

        //增加文库月统计访问信息
        monthReportService.addDocViews();

        return docDao.find(id);
    }

    public Doc get(Long id) {
        return docDao.find(id);
    }

    public Page<Doc> searchWeb(Map<String, Object> parameters) {
        parameters.put("state", Const.DOC_STATE_NORMAL);
        // 分词查询
        if (parameters.containsKey("content")) {

            String content = (String) parameters.get("content");

            content = synonymService.getSynonymsRegexp(content);
            parameters.put("content_sort", content.split("\\|"));
            parameters.put("content", synonymService.getSynonymsRegexp(content));
        }
        if (parameters.containsKey("docType")) {
            String docType = (String) parameters.get("docType");

            //System.out.println("docType:"+docType);
            docType = synonymService.getSynonymsRegexp(docType);
            parameters.put("content_sort", docType.split("\\|"));
            parameters.put("docType", synonymService.getSynonymsRegexp(docType));
        }

        if (parameters.containsKey("firstClassfy")) {
            String firstClassfy = (String) parameters.get("firstClassfy");

            System.out.println("firstClassfy:" + firstClassfy);
            firstClassfy = synonymService.getSynonymsRegexp(firstClassfy);
            parameters.put("content_sort", firstClassfy.split("\\|"));
            parameters.put("firstClassfy", synonymService.getSynonymsRegexp(firstClassfy));
        }

        if (parameters.containsKey("secondClassfy")) {
            String secondClassfy = (String) parameters.get("secondClassfy");

            System.out.println("secondClassfy:" + secondClassfy);
            secondClassfy = synonymService.getSynonymsRegexp(secondClassfy);
            parameters.put("content_sort", secondClassfy.split("\\|"));
            parameters.put("secondClassfy", synonymService.getSynonymsRegexp(secondClassfy));
        }

        Pageable pageable = getPageable(parameters);
        long count = docDao.countWeb(parameters);
        List<Doc> userJobFeeList = docDao.searchWeb(parameters);
        return new PageImpl<>(userJobFeeList, pageable, count);
    }


    public List<Doc> searchRecordAjax(DocCategory docCategory) {

        return docDao.searchRecordAjax(docCategory);
    }

    public Integer searchRecordAjaxCount(DocCategory docCategory) {

        return docDao.searchRecordAjaxCount(docCategory);
    }


    public long countWeb(Map<String, Object> parameters) {
        return docDao.countWeb(parameters);
    }

    public Doc findWeb(Long id) {
        Doc doc = docDao.findWeb(id);
        doc.setViews(doc.getViews() + 1);
        docDao.update(doc);
        return doc;
    }

    @Transactional
    public void create(Doc doc) {
        doc.setState(Const.DOC_STATE_NORMAL);
        doc.setViews(0L);
        doc.setCreateTime(new Date());
        docDao.create(doc);
        setDocTags(doc);
    }

    @Transactional
    public boolean update(Doc doc) {
        setDocTags(doc);
        return docDao.update(doc) > 0;
    }

    public boolean delete(Long id) {
        Doc doc = new Doc();
        doc.setId(id);
        doc.setState(Const.DOC_STATE_DELETE);
        return docDao.update(doc) > 0;
    }

    @Transactional
    private void setDocTags(Doc doc) {
        if (doc.getTags() == null) {
            return;
        }
        docTagDao.deleteDocXTagsByDocId(doc.getId());
        if (!doc.getTags().isEmpty()) {
            List<String> tags = doc.getTags().stream().filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            DocTag tag;
            List<DocXTag> axts = new ArrayList<>();
            for (String t : tags) {
                tag = docTagDao.find(t);
                if (tag == null) {
                    tag = new DocTag(t);
                    docTagDao.create(tag);
                }
                axts.add(new DocXTag(doc.getId(), tag.getId()));
            }
            if (!axts.isEmpty()) {
                docTagDao.createDocXTags(axts);
            }
        }
    }

    // 用户上传文件
    @Transactional
    public Doc uploadWithInfo(MultipartFile file, Doc doc) throws IOException {
        create(doc);
        doc = upload(file, doc);
        if (doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
            userScoreDao.updateScore(doc.getUploader(), Const.DOC_UPLOAD_SCORE);
            userScoreHistoryDao.create(new UserScoreHistory(doc.getUploader(), Const.USER_SCORE_HISTORY_TYPE_UPLOAD, Const.DOC_UPLOAD_SCORE, doc.getId()));
        }
        return doc;
    }

    // 用户上传文档文件
    @Transactional
    public Doc upload(MultipartFile file, Doc doc) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileType = FilenameUtils.getExtension(fileName);
        if (!fileType.toLowerCase().matches(Const.DOC_ALLOW_TYPES.replaceAll(",", "|"))) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10107);
        }
        if (fileType.equals("jpeg")) {
            fileType = "jpg";
        }
        String docPath = "doc/" + doc.getId() + "/" + UUID.randomUUID().toString() + "." + fileType;
        doc.setFileMd5(DigestUtils.md5Hex(file.getBytes()));

        // 如果是自己上传过的文件则提示文件已存在
        if (docDao.findMd5File(doc.getUploader(), doc.getUploaderType(), doc.getFileMd5())) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10106);
        }

        File docFile = new File(configer.getFileBasePath() + docPath);
        if (docFile.getParentFile().exists() || docFile.getParentFile().mkdirs())
            file.transferTo(docFile);
        doc.setFileName(file.getOriginalFilename());
        doc.setFilePath(docPath);
        Long fileSize = (long) Math.ceil(file.getSize() / 1024);
        doc.setFileSize(fileSize);
        doc.setFileType(fileType);
        doc.setState(Const.DOC_STATE_LOADING);
        update(doc);
        // 使用异步线程分析保存Doc图片
        taskExecutor.execute(() -> saveDocImages(doc, docFile));
        return doc;
    }

    // 保存Doc图片
    @Transactional
    private void saveDocImages(Doc doc, File file) {
        List<String> images;
        switch (doc.getFileType()) {
            case "pdf":
                images = saveDocPdfImages(doc, file);
                break;
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
                images = saveDocOfficeImages(doc, file);
                break;
            default:
                images = new ArrayList<>();
        }
        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                String img = "doc/" + doc.getId() + "/" + images.get(i);
                doc.addImage(img, i + 1);
            }
        }
        if (docDao.find(doc.getId()) != null) {
            doc.setState(Const.DOC_STATE_NORMAL);
            docDao.updateDocState(doc.getId(), doc.getState());
            if (!doc.getImages().isEmpty())
                docImageDao.save(doc.getImages());
        } else {
            deleteDocImages(doc);
        }
    }

    // 删除文档图片
    private void deleteDocImages(Doc doc) {
        List<DocImage> images = docImageDao.findByDocId(doc.getId());
        File file;
        for (DocImage image : images) {
            file = new File(configer.getFileBasePath() + image.getPath());
            if (file.exists() && file.isFile()) {
                FileUtils.deleteQuietly(file);
            }
        }
        docImageDao.deleteByDocId(doc.getId());
        doc.setImages(null);
    }

    // 解析PDF为图片并保存
    private List<String> saveDocPdfImages(Doc doc, File file) {
        deleteDocImages(doc);
        String imagePath = configer.getFileBasePath() + "doc/" + doc.getId();
        return DocUtils.convertPdf2Images(file, imagePath, Const.DOC_IMG_PAGES);
    }

    // 解析PPT(X)为图片并保存
    private List<String> saveDocOfficeImages(Doc doc, File file) {
//        deleteDocImages(doc);
//        String imagePath = configer.getFileBasePath() + "doc/" + doc.getId();
//        return DocUtils.convertOffice2Images(documentConverter, file, imagePath, Const.DOC_IMG_PAGES, false);

        return null;
    }

    // 读取txt文件内容
    public String getTxtContent(File file, int size) {
        try {
            String charset = DocUtils.getTxtFileCharset(file);
            String content = FileUtils.readFileToString(file, charset);
            if (content != null) {
                if (content.length() > size) {
                    content = content.substring(0, size) + "……";
                }
                return "<p>" + content.replaceAll("[\r\n]+", "</p><p>") + "</p>";
            }
        } catch (IOException e) {
            logger.warn("读取TXT文件内容失败！");
        }
        return null;
    }

    //获取用户收藏的文章
    public List<DocDto> getCollects(long userId) {
        return docDao.getCollects(userId);
    }

    //获取用户下载过的文章
    public List<DocDto> getDownloads(long userId) {
        return docDao.getDownloads(userId);
    }

    //获取用户上传的文章
    public List<Doc> getUploads(Map<String, Object> param) {
        return docDao.search(param);
    }

    // 用户下载文档文件
    @Transactional
    public File download(User user, Doc doc) {
        if (doc.getFilePath() == null) {
            logger.warn("文档没有可下载的文件：" + doc.getId());
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        String filePath = configer.getFileBasePath() + doc.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) {
            logger.warn("文档的文件不存在：" + doc.getFilePath());
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404).setData(filePath);
        }
        return file;
    }

    // 添加下载记录和下载积分计算
    @Transactional
    public void downloadRecord(User user, Doc doc) {
        // 扣分条件：1.需要积分, 2.不是自己的文件
        if (!docDownloadDao.hasDownload(user.getId(), doc.getId())) {
            DocDownload download = new DocDownload(user.getId(), doc.getId(), doc.getCostScore());
            docDownloadDao.create(download);

            if (doc.getCostScore() > 0 && (doc.getUploaderType() != Const.DOC_UPLOADERTYPE_USER || !user.getId().equals(doc.getUploader()))) {
                UserScore userScore = userScoreDao.find(user.getId());
                if (userScore.getTotalScore() < doc.getCostScore()) {       //积分不足
                    throw new ServerException(HttpStatus.FORBIDDEN, M.E20001);
                }

                // 给下载者扣分
                userScoreDao.updateScore(user.getId(), -doc.getCostScore());
                userScoreHistoryDao.create(new UserScoreHistory(user.getId(), Const.USER_SCORE_HISTORY_TYPE_DOWNLOAD, -doc.getCostScore(), download.getId()));

                // 给上传者加分
                if (doc.getUploaderType() == Const.DOC_UPLOADERTYPE_USER) {
                    userScoreDao.updateScore(doc.getUploader(), doc.getCostScore());
                    userScoreHistoryDao.create(new UserScoreHistory(doc.getUploader(), Const.USER_SCORE_HISTORY_TYPE_SALE, doc.getCostScore(), download.getId()));
                }
            }
        }
    }

    public List<DocImage> findByDocId(long docId) {
        return docImageDao.findByDocId(docId);
    }

    public List<DocDownload> docDownloadsearch() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("limit", 5);
        parameters.put("offset", 1);
        return docDownloadDao.search(parameters);
    }

    public long getDownloadCount(Map<String, Object> params) {
        return docDownloadDao.count(params);
    }

    //首页数据
    public List<Doc> searchForIndex(Map<String, Object> params) {
        return docDao.searchForIndex(params);
    }
}
