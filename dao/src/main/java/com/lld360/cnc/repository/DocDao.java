package com.lld360.cnc.repository;

import java.util.List;
import java.util.Map;

import com.lld360.cnc.dto.DocDto;
import com.lld360.cnc.model.CncDaysStatistics;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.DocCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocDao {

    List<Doc> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    long countByKeyWord(String keyword);

    long countDownloads(Map<String, Object> parameters);

    Doc find(Long id);

    List<Doc> searchWeb(Map<String, Object> parameters);

    long countWeb(Map<String, Object> parameters);

    Doc findWeb(Long id);

    void create(Doc doc);

    int update(Doc doc);

    void delete(Long id);

    List<DocDto> getCollects(long userId);

    List<DocDto> getDownloads(long userId);

    List<Doc> searchForIndex(Map<String, Object> parameters);

    List<Doc> searchRecordAjax(DocCategory docCategory);

    Integer searchRecordAjaxCount(DocCategory docCategory);

    boolean findMd5File(@Param("uploader") Long uploader, @Param("uploaderType") Byte uploaderType, @Param("md5") String md5);

    void updateDocState(@Param("docId") Long docId, @Param("state") byte state);
}