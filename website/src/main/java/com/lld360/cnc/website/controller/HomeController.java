package com.lld360.cnc.website.controller;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.DocImage;
import com.lld360.cnc.model.ThirdAccount;
import com.lld360.cnc.service.DocCategoryService;
import com.lld360.cnc.service.DocService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.dto.QqAccountAccessToken;
import com.lld360.cnc.website.dto.WxAccountAccessToken;
import com.lld360.cnc.website.service.ThirdAccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-08-29 15:39
 */
@Controller
@RequestMapping("/")
public class HomeController extends SiteController {
    @Autowired
    private ThirdAccountService thirdAccountService;

    @Autowired
    private DocCategoryService docCategoryService;

    @Autowired
    private DocService docService;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(Model model, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        if (StringUtils.isEmpty(code) && state != null) {
            return "redirect:/m/login";
        }
        String weixinState = getSessionStringValue("weixinState");
        ThirdAccount thirdAccount = null;
        int type;
        if (state != null && weixinState != null && state.equals(weixinState)) {
            WxAccountAccessToken token = thirdAccountService.getWxAccessToken(code);
            if (token != null) {
                thirdAccount = thirdAccountService.getWxUserinfo(token);
                if (!thirdAccountService.findByOpenidCount(thirdAccount.getOpenid())) {
                    thirdAccountService.create(thirdAccount);
                } else {
                    thirdAccount = thirdAccountService.findByOpenid(thirdAccount.getOpenid());
                    thirdAccount.setUpdateTime(new Date());
                    thirdAccountService.update(thirdAccount);
                }
            }
        }
        String qqState = getSessionStringValue("qqState");
        if (state != null && qqState != null && state.equals(qqState)) {
            QqAccountAccessToken token = thirdAccountService.getQqAccessToken(code);
            if (token != null) {
                token = thirdAccountService.getQqOpenId(token);
                if (token.getOpenid() != null) {
                    thirdAccount = thirdAccountService.getQqUserinfo(token);
                    type = thirdAccount.getType();
                    if (thirdAccount != null && !thirdAccountService.findByOpenidCount(thirdAccount.getOpenid())) {
                        thirdAccount.setType(type);
                        thirdAccountService.create(thirdAccount);
                    } else {
                        thirdAccount = thirdAccountService.findByOpenid(thirdAccount.getOpenid());
                        thirdAccount.setUpdateTime(new Date());
                        thirdAccount.setType(type);
                        thirdAccountService.update(thirdAccount);

                    }
                }
            }
        }
        if (thirdAccount != null) {
            if (thirdAccount.getUserId() == null) {
                request.getSession().setAttribute(Const.SS_USER, thirdAccountService.thirdAccountLogin(thirdAccount));
            } else {
                request.getSession().setAttribute(Const.SS_USER, userService.getUserDto(thirdAccount.getUserId()));
            }
        }
        model.addAttribute("configer", configer);

        Map<String, Object> paramForRecommend = new HashMap<>();
        model.addAttribute("userCount", userService.getCount(paramForRecommend));       //用户数量

        paramForRecommend.put("sortBy", "create_time");
        paramForRecommend.put("limit", 5);
        List<Doc> leftRecommendDocs = docService.searchForIndex(paramForRecommend);
        getMistiming(leftRecommendDocs);
        model.addAttribute("leftRecommendDocs", leftRecommendDocs);      //左边每日推荐

        paramForRecommend.put("offset", 5);
        List<Doc> rightRecommendDocs = docService.searchForIndex(paramForRecommend);
        getMistiming(rightRecommendDocs);
        model.addAttribute("rightRecommendDocs", rightRecommendDocs);     //右边每日推荐

        Map<String, Object> paramForHot = new HashMap<>();
        paramForHot.put("sortBy", "views");
        paramForHot.put("limit", 3);
        model.addAttribute("hotDocs", docService.searchForIndex(paramForHot));  //热门推荐

        String[] types = {"pdf", "doc", "xls", "ppt", "txt", "jpg"};        //分类列表
        paramForHot.replace("limit", 6);
        for (String fileType : types) {
            paramForHot.put("fileType", fileType);
            List<Doc> docs = docService.searchForIndex(paramForHot);
            for (Doc doc : docs) {
                List<DocImage> images = docService.findByDocId(doc.getId());
                if (images.size() != 0) {
                    doc.setImagePath(images.get(0).getPath());
                }
            }
            model.addAttribute(fileType + "Docs", docs);
        }

        List<DocCategory> docCategories = docCategoryService.getAll();        //推荐模块
        model.addAttribute("docCategories", docCategories);

        return "index";
    }

    //获取已上传时间差
    private void getMistiming(List<Doc> docs) {
        for (Doc doc : docs) {
            Date createTime = doc.getCreateTime();
            Date nowTime = new Date();
            if (createTime == null) {
                doc.setDiffTime("一年");
                continue;
            }

            long diffTime = nowTime.getTime() - createTime.getTime();
            long minutes = diffTime / (1000 * 60);
            long hours = minutes / 60;
            long days = hours / 24;
            long months = days / 30;

            if (minutes == 0) {
                doc.setDiffTime("刚刚");
            } else if (hours == 0) {
                doc.setDiffTime(minutes + "分钟");
            } else if (days == 0) {
                doc.setDiffTime(hours + "小时");
            } else if (months == 0) {
                doc.setDiffTime(days + "天");
            } else if (months >= 12) {
                doc.setDiffTime("一年");
            } else {
                doc.setDiffTime(months + "个月");
            }
        }
    }

}
