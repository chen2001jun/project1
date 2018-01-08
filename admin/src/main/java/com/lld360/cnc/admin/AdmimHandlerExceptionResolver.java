package com.lld360.cnc.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.vo.ResultOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author: dhc
 * Date: 2016-07-18 14:41
 */
public class AdmimHandlerExceptionResolver implements HandlerExceptionResolver {
    Logger logger = LoggerFactory.getLogger(AdmimHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        PrintWriter writer = null;
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        RequestContext ctx = new RequestContext(request);

        ResultOut out = new ResultOut();
        if (ex instanceof ServerException) {
            ServerException e = (ServerException) ex;

            String message;
            if (e.getCode() != null) {
                message = ctx.getMessage(String.valueOf(e.getCode()), e.getArgs());
            } else if (e.getMessage() != null) {
                e.setCode(M.E90002);
                message = e.getMessage();
            } else {
                e.setCode(M.E90002);
                message = e.getStatus().name();
            }
            out.setCode(e.getCode());
            out.setMessage(message);
            if (e.getData() != null) {
                out.setData(e.getData());
            }
            response.setStatus(e.getStatus().value());
        } else {
            String message = ex.getMessage();
            if (message == null) {
                message = ex.getClass().getName();
            }
            out.setCode(M.S90500.getCode());
            out.setMessage(ctx.getMessage(M.S90500.toString()));
            out.setData(message);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            logger.error("★★★发现未处理异常：", ex);
        }

        try {
            writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            writer.print(mapper.writeValueAsString(out));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return null;
    }
}
