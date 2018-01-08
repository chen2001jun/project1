package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.Article;
import com.lld360.cnc.model.Setting;
import com.lld360.cnc.service.ArticleService;
import com.lld360.cnc.service.SettingService;
import com.lld360.cnc.service.WxGzhService;
import com.lld360.cnc.website.SiteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Author: dhc
 * Date: 2016-07-07 17:29
 */
@Controller
@RequestMapping("m/article")
public class MArticleController extends SiteController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private WxGzhService wxGzhService;

    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("articles", articleService.getArticlesByKeyWord(keyword));
        return "m/article-list";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String find(@PathVariable long id, Model model) {
        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("images", article.getImages().split(","));
        model.addAttribute("jsTicket", wxGzhService.getWxGzhJsApiTicket().getTicket());
        return "m/article-detail";
    }

    @RequestMapping(value = "help", method = RequestMethod.GET)
    public String help(Model model) {
        Setting s = settingService.get(Const.SETTING_CNC_HELP_MPAGE_CONTENT);
        model.addAttribute("s", s);
        return "m/help";
    }
}
