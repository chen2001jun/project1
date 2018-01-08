package com.lld360.cnc;

import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.ServletWired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-06-30 10:06
 */
public abstract class BaseService extends ServletWired {

    protected Pageable getPageable(Map<String, Object> params) {
        int page = params.containsKey(Const.PAGE_PAGE) ? (int) params.get(Const.PAGE_PAGE) : 1;
        int size = params.containsKey(Const.PAGE_SIZE) ? (int) params.get(Const.PAGE_SIZE) : 15;
        return new PageRequest(page - 1, size);
    }

    //参数封装
    public Map<String, Object> getParameters(Object... params) {
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < params.length; i = i + 2) {
            parameters.put((String) params[i], params[i + 1]);
        }
        return parameters;
    }

    @SuppressWarnings("unchecked")
    protected <T> T setValueIfEmptyKey(Map<String, Object> map, String key, T t) {
        if (!map.containsKey(key) || map.get(key) == null) {
            map.put(key, t);
            return t;
        }
        return (T) map.get(key);
    }

    /**
     * 获取完整路径
     *
     * @param absolutePath 相对路径
     * @return 完整路径
     */
    public String getFullUrl(String absolutePath) {
        if (absolutePath.startsWith("http[s]?://")) {
            return absolutePath;
        }
        if (absolutePath.startsWith("/\\w+"))
            absolutePath = absolutePath.substring(1);
        return configer.getAppUrl() + absolutePath;
    }

    public Map<String, Object> generateParamsMap(Object... params){
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < params.length; i = i + 2) {
            parameters.put((String) params[i], params[i + 1]);
        }
        return parameters;
    }
}
