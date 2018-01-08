package com.lld360.cnc.core.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Author: dhc
 * Date: 2016-08-30 14:13
 */
public class ClientUtils {

    /**
     * 判断是否jQuery的Ajax请求
     *
     * @param request 请求对象
     * @return 判断结果
     */
    public static boolean isAjax(HttpServletRequest request) {
        String xrw = request.getHeader("X-Requested-With");
        return xrw != null && xrw.equals("XMLHttpRequest");
    }

    // 获取浏览器标识符
    public static String getUserAgent(HttpServletRequest request) {
        Enumeration<String> userAgents = request.getHeaders("User-Agent");
        if (userAgents.hasMoreElements()) {
            return userAgents.nextElement();
        }
        return null;
    }

    // 判断是否手机浏览器
    public static boolean isMobileBrowser(HttpServletRequest request) {
        String agent = getUserAgent(request);
        if (agent != null) {
            String[] uaKeys = "Mobile,Android,iPhone,iPod,Windows Phone,MQQBrowser,MicroMessenger".split(",");
            for (String key : uaKeys) {
                if (agent.contains(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据名称获取指定Cookie
     *
     * @param request 用户请求
     * @param name    Cookie名称
     * @return 需要取得的Cookie
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
