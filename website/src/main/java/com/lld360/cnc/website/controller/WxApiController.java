package com.lld360.cnc.website.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.lld360.cnc.service.WxGzhService;
import com.lld360.cnc.vo.WxGzhJsConfig;
import com.lld360.cnc.vo.WxGzhMessage;
import com.lld360.cnc.website.SiteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: dhc
 * Date: 2016-06-29 16:32
 */
@Controller
@RequestMapping("weixin")
public class WxApiController extends SiteController {

    @Autowired
    private XmlMapper xmlMapper;

    @Autowired
    private WxGzhService wxGzhService;

    @ResponseBody
    @RequestMapping(value = "signature", method = RequestMethod.POST)
    public WxGzhJsConfig getWxJsSignature(@RequestParam String url) {
        return wxGzhService.getWxGzhJsConfig(url);
    }

    // 验证微信服务器
    @RequestMapping(value = "api", method = RequestMethod.GET)
    public void validGet(@RequestParam String signature, // 微信加密签名
                         @RequestParam String timestamp, // 时间戳
                         @RequestParam String nonce,     // 随机数
                         @RequestParam String echostr,   // 随机字符串
                         HttpServletResponse response) throws IOException {
        String result = wxGzhService.wxServerValid(signature, timestamp, nonce) ? echostr : "invalid request";
        outText(response, result);
    }

    /**
     * 回复消息
     *
     * @param message  接收到的消息
     * @param response 系统回函
     */
    @RequestMapping(value = "api", method = RequestMethod.POST)
    public void messagePost(@RequestBody WxGzhMessage message, HttpServletResponse response) {
        logger.info("" + message.getFromUserName() + " >>> " + message.getContent());

        WxGzhMessage back = wxGzhService.getBackMessage(message);

        response.setContentType(MediaType.TEXT_XML_VALUE);
        response.setCharacterEncoding("utf-8");

        String result = "";
        try {
            result = xmlMapper.writeValueAsString(back).replace(" xmlns=\"\"", "");
        } catch (JsonProcessingException e) {
            logger.warn("转换对象错误：" + e.getMessage(), e);
        }
        outText(response, result);
    }
}
