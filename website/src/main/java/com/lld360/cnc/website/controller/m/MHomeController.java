package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.model.ThirdAccount;
import com.lld360.cnc.repository.DocCategoryDao;
import com.lld360.cnc.service.DocService;
import com.lld360.cnc.service.SearchWordsService;
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
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-08-01 14:40
 */
@Controller
@RequestMapping("m")
public class MHomeController extends SiteController {

    @Autowired
    private DocCategoryDao docCategoryDao;

    @Autowired
    private DocService docService;

    @Autowired
    private SearchWordsService searchWordsService;

    @Autowired
    private ThirdAccountService thirdAccountService;

    @Autowired
    private UserService userService;

    @RequestMapping("")
    public String indexGet(Model model, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        if (StringUtils.isEmpty(code) && state != null) {
            return "redirect:/m/login";
        }
        String weixinState = getSessionStringValue("weixinState");
        ThirdAccount thirdAccount = null;
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
                    if (thirdAccount != null) {
                        if (!thirdAccountService.findByOpenidCount(thirdAccount.getOpenid()))
                            thirdAccountService.create(thirdAccount);
                        else {
                            thirdAccount = thirdAccountService.findByOpenid(thirdAccount.getOpenid());
                            thirdAccount.setUpdateTime(new Date());
                            thirdAccountService.update(thirdAccount);
                        }
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
        model.addAttribute("categorys", docCategoryDao.getAll());
        Map<String, Object> params = getParamsPageMap(10);
        model.addAttribute("searchWords", searchWordsService.getSearchingWords(getParamsPageMap(5)));
        params.put("hotType", "hotType");
        model.addAttribute("docs", docService.searchWeb(params));
        model.addAttribute("downloads", docService.docDownloadsearch());
        model.addAttribute("configer", configer);
        return "m/index";
    }


}
