package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.model.DocCollect;
import com.lld360.cnc.repository.DocCollectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DocCollectService extends BaseService {

    @Autowired
    private DocCollectDao docCollectDao;

    public void docCollect(Long userId, Long docId) {
        DocCollect docCollect = new DocCollect();
        docCollect.setDocId(docId);
        docCollect.setUserId(userId);
        docCollect.setCreateTime(new Date());
        docCollectDao.create(docCollect);
    }

    public void uncollect(long userId, long docId) {
        Map<String, Object> params = generateParamsMap("userId", userId, "docId", docId);
        if (docCollectDao.find(params) == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        } else {
            docCollectDao.delete(params);
        }
    }

    public String isCollect(Long userId, Long docId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("docId", docId);
        return docCollectDao.find(params) != null ? "collect" : "notCollect";
    }

    public long conllectCount(Map<String, Object> params) {
        return docCollectDao.count(params);
    }
}
