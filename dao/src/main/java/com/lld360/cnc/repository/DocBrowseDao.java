package com.lld360.cnc.repository;

import com.lld360.cnc.model.DocBrowse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * DocBrowse 数据库操作
 */
@Repository
public interface DocBrowseDao {

    List<DocBrowse> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    void save(DocBrowse docbrowse);

}