package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.core.Configer;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.website.SiteController;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("m/auth")
public class MAuthController extends SiteController {

    @Autowired
    UserService userService;

    @Autowired
    Configer configer;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(Model model) {
        if(getCurrentUser() != null) {
            return "redirect:/m/";
        }
        model.addAttribute("configer", configer);
        request.getSession().setAttribute("weixinState", "weixin_" + RandomStringUtils.randomAlphanumeric(12));
        request.getSession().setAttribute("qqState", "qq_" + RandomStringUtils.randomAlphanumeric(12));
        return "m/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletResponse response) {
        userService.doLogout(response);
        return "redirect:/m/auth/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String toRegisterPage() {
        return "m/register";
    }

}
