package com.lld360.cnc.repository;

import java.util.List;
import java.util.Map;

import com.lld360.cnc.model.WxGzh;
import org.springframework.stereotype.Repository;

/**
 * WxGzh 数据库操作
 */
@Repository
public interface WxGzhDao {
	
    List<WxGzh> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    WxGzh find(Long id);

    void create(WxGzh wxGzh);

    int update(WxGzh wxGzh);

    void delete(Long id);

}