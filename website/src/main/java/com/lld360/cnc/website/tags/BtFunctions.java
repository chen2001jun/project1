package com.lld360.cnc.website.tags;

/**
 * Author: dhc
 * Date: 2016-08-31 11:01
 */
public class BtFunctions {
//    private static HttpServletRequest getRequest() {
//        ServletRequestAttributes requestAttribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        return requestAttribute.getRequest();
//    }

    public static String outVal(String s, String defVal) {
        return s == null || s.length() == 0 ? defVal : s;
    }

    public static String concat(Object... os) {
        StringBuilder sb = new StringBuilder();
        for (Object o : os) {
            sb.append(o.toString());
        }
        return sb.toString();
    }

    public static String url(String url, String localPrefix, String defaultUrl) {
        if (url == null || url.equals("")) {
            return defaultUrl == null ? "" : defaultUrl;
        }
        if (url.startsWith("http") || localPrefix == null) {
            return url;
        }
        return localPrefix + url;
    }
}
