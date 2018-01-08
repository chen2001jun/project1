package com.lld360.cnc.website;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.utils.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerExceptionResolverComposite;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: dhc
 * Date: 2016-07-18 14:41
 */
public class SiteHandlerExceptionResolver extends HandlerExceptionResolverComposite {
    Logger logger = LoggerFactory.getLogger(SiteHandlerExceptionResolver.class);

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        RequestContext ctx = new RequestContext(request);

        ServerException e = null;
        if (ex instanceof ServerException) {
            e = (ServerException) ex;

            if (e.getCode() != null) {
                e.setMessage(ctx.getMessage(String.valueOf(e.getCode()), e.getArgs()));
            } else if (e.getMessage() != null) {
                e.setCode(M.E90002);
            } else {
                e.setCode(M.E90002);
                e.setMessage(e.getStatus().name());
            }
        } else {
            logger.error("★★★发现未处理异常：", ex);
        }

        // 进行Ajax输出
        if (ClientUtils.isAjax(request)) {
            ModelAndView view = new ModelAndView(new MappingJackson2JsonView(objectMapper));
            if (e != null) {
                view.addObject("code", e.getCode());
                view.addObject("message", e.getMessage());
                if (e.getData() != null) {
                    view.addObject("message", e.getData());
                }
                response.setStatus(e.getStatus().value());
            } else {
                String message = ex.getMessage();
                if (message == null) {
                    message = ex.getClass().getName();
                }
                view.addObject("code", M.S90500.getCode());
                view.addObject("message", ctx.getMessage(M.S90500.toString()));
                view.addObject("data", message);
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            return view;
        } else if (e != null) {
            if (e.getStatus() == HttpStatus.UNAUTHORIZED) {
                String loginUrl = ClientUtils.isMobileBrowser(request) ? "/m/auth/login" : "/auth/login";
                return new ModelAndView("redirect:" + loginUrl);
            }
            return new ModelAndView("errors/xxx").addObject("e", e);
        }
        return super.resolveException(request, response, o, ex);
    }
}
