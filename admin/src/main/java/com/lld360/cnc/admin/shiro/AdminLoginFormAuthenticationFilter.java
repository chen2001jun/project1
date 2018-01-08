package com.lld360.cnc.admin.shiro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.model.Admin;
import com.lld360.cnc.service.AdminService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-07-13 14:26
 */
public class AdminLoginFormAuthenticationFilter extends FormAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(AdminLoginFormAuthenticationFilter.class);

    @Autowired
    AdminService adminService;

    private void outJson(HttpServletResponse response, HttpStatus status, Object o) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(status.value());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(mapper.writeValueAsString(o));
            writer.flush();
        } catch (IOException e) {
            logger.warn("输出异常：" + e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        Map<String, String> result = new HashMap<>();
        result.put("message", "帐号和密码错误！");
        outJson((HttpServletResponse) response, HttpStatus.UNAUTHORIZED, result);
        return true;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        Admin admin = (Admin) subject.getPrincipal();
        admin.setLastLogin(new Date());
        adminService.update(admin);
        outJson((HttpServletResponse) response, HttpStatus.OK, admin);
        return false;
    }
}
