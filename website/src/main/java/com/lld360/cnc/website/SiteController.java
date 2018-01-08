package com.lld360.cnc.website;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: dhc
 * Date: 2016-06-29 13:44
 */
public abstract class SiteController extends BaseController {

    protected UserDto getCurrentUser() {
        return getSessionObject(Const.SS_USER);
    }

    protected UserDto getRequiredCurrentUser() {
        UserDto user = getCurrentUser();
        if (user == null) {
            throw new ServerException(HttpStatus.UNAUTHORIZED).setMessage("用户未登录");
        }
        return user;
    }

    // 获取Session中的String值
    protected String getSessionStringValue(String key) {
        Object o = request.getSession().getAttribute(key);
        return o == null ? null : o.toString();
    }

    // 获取Session中的Number值
    protected Number getSessionNumberValue(String key) {
        Object o = request.getSession().getAttribute(key);
        if (o != null) {
            return (Number) o;
        }
        return null;
    }

    // 获取Session中的Number值，如果获取失败则使用默认值
    protected Number getSessionNumberValue(String key, Number def) {
        Number number = getSessionNumberValue(key);
        return number == null ? def : number;
    }

    // 从Session中获取对象
    @SuppressWarnings("unchecked")
    protected <T> T getSessionObject(String key) {
        Object o = request.getSession().getAttribute(key);
        if (o != null) {
            return (T) o;
        }
        return null;
    }

    // 拦截SpringMVC的常用异常信息
    // MissingServletRequestParameterException 400 参数不正确
    // NoSuchRequestHandlingMethodException 404 请求地址不存在
    // HttpRequestMethodNotSupportedException 405 方法不支持

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResultOut> msrpExceptionHandler(MissingServletRequestParameterException e, HttpServletResponse response) {
        ResultOut out = new ResultOut(M.S90400, e.getMessage());
        return new ResponseEntity<>(out, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResultOut> hrmnsExceptionHandler(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
        ResultOut out = new ResultOut(M.S90405, e.getMessage());
        return new ResponseEntity<>(out, HttpStatus.BAD_REQUEST);
    }
}
