package com.lld360.cnc.repository;

import com.lld360.cnc.model.DocCategory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * DocCategory 数据库操作
 */
@Repository
public interface DocCategoryDao {

    DocCategory find(int id);

    void create(DocCategory doccategory);

    int update(DocCategory doccategory);

    void delete(int id);

    List<DocCategory> getAll();

    List<DocCategory> getAllCategory();

    List<DocCategory> getAllByFidCategory(DocCategory docCategory);

    List<DocCategory> getAllOtherCategory(DocCategory docCategory);

    List<DocCategory> searchFirst(Map<String, Object> parameters);

    List<DocCategory> searchSecond(DocCategory docCategory);
    long countFirst(Map<String, Object> parameters);


}