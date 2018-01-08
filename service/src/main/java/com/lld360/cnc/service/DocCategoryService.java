package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.SearchWords;
import com.lld360.cnc.repository.DocCategoryDao;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class DocCategoryService extends BaseService {

    @Autowired
    private DocCategoryDao docCategoryDao;

    public Page<DocCategory> getSearchingFirstCategory(Map<String, Object> params) {
        Pageable pageable = getPageable(params);
        long count = docCategoryDao.countFirst(params);
        List<DocCategory> searches = count > 0 ? docCategoryDao.searchFirst(params) : new ArrayList<>();
        return new PageImpl<>(searches, pageable, count);
    }

    public List<DocCategory> getSearchingSecondCategory(DocCategory docCategory) {


        return docCategoryDao.searchSecond(docCategory);
    }

    // 层级获取
    public List<DocCategory> getAll() {
        return docCategoryDao.getAll();
    }

    // 列表获取
    public List<DocCategory> getAllCategory() {
        return docCategoryDao.getAllCategory();
    }

    private void deleteOldIcon(DocCategory category) {
        if (category.getIcon() != null) {
            File oldIcon = new File(configer.getFileBasePath() + category.getIcon());
            if (oldIcon.exists())
                FileUtils.deleteQuietly(oldIcon);
        }
    }

    // 保存类型
    public void save(DocCategory category, MultipartFile icon) {
        if (icon != null) {
            String relativeFile = "docCategory/icon_" + RandomStringUtils.randomAlphanumeric(6)
                    + "." + FilenameUtils.getExtension(icon.getOriginalFilename());
            String absoluteFile = configer.getFileBasePath() + relativeFile;
            deleteOldIcon(category);
            try {
                File f = new File(absoluteFile);
                if (f.getParentFile().exists() || f.getParentFile().mkdirs())
                    icon.transferTo(f);
                category.setIcon(relativeFile);
            } catch (IOException e) {
                logger.warn("保存文档分类图标失败");
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E90003).setData(absoluteFile);
            }
        }
        if (category.getId() == null) {
            docCategoryDao.create(category);
        } else {
            docCategoryDao.update(category);
        }
    }

    public DocCategory get(Integer id) {
        return docCategoryDao.find(id);
    }

    public void delete(int id) {
        DocCategory category = docCategoryDao.find(id);
        if (category != null) {
            docCategoryDao.delete(id);
            deleteOldIcon(category);
        }
    }
}
