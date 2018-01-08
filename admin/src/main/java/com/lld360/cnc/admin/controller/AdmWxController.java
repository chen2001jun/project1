package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.service.WxGzhService;
import com.lld360.cnc.vo.WxApiBack;
import com.lld360.cnc.vo.WxGzhButton;
import com.lld360.cnc.vo.WxGzhMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author: dhc
 * Date: 2016-07-12 14:35
 */
@RestController
@RequestMapping("admin/wx")
public class AdmWxController extends AdmController {

    @Autowired
    private WxGzhService wxGzhService;

    // 获取微信按钮
    @RequestMapping(value = "buttons", method = RequestMethod.GET)
    public List<WxGzhButton> menuButtonGet() {
        return wxGzhService.getWxGzhButtons();
    }

    // 设置微信按钮
    @RequestMapping(value = "buttons", method = RequestMethod.POST)
    public WxApiBack menuButtonPost(@RequestBody List<WxGzhButton> buttons) {
        return wxGzhService.setWxGzhButtons(buttons);
    }

    // 获取公众号回复消息默认配置
    @RequestMapping(value = "articles", method = RequestMethod.GET)
    public List<WxGzhMessage.Article> messageArticleSettingGet() {
        return wxGzhService.getWxGzhMessageArticleSetting();
    }

    // 获取公众号回复消息默认配置
    @RequestMapping(value = "articles", method = RequestMethod.POST)
    public List<WxGzhMessage.Article> messageArticleSettingPost(@RequestBody List<WxGzhMessage.Article> articles) {
        return wxGzhService.setWxGzhMessageArticleSetting(articles);
    }

    // 上传回复文章列表图标
    @RequestMapping(value = "articleIcon/{idx}", method = RequestMethod.POST)
    public List<WxGzhMessage.Article> docFilePost(@PathVariable int idx, @RequestParam MultipartFile file) {
        return wxGzhService.setWxGzhMessageArticleImage(idx, file);
    }
}
