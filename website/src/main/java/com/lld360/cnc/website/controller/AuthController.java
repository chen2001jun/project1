package com.lld360.cnc.website.controller;

import com.gonvan.kaptcha.Constants;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.service.ValidSmsService;
import com.lld360.cnc.website.SiteController;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("auth")
public class AuthController extends SiteController {

    @Autowired
    UserService userService;

    @Autowired
    ValidSmsService validSmsService;

    //web注册页面
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String registGet() {
        return "wAuth/register1";
    }

    // 进行注册
    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public User registPost(@RequestParam String mobile, @RequestParam String password, @RequestParam String validCode) {
        UserDto user = userService.registerWithMobile(mobile, password, validCode);
        request.getSession().setAttribute(Const.SS_USER, user);
        return user;
    }

    //web注册页面2
    @RequestMapping(value = "register2", method = RequestMethod.GET)
    public String regist2Get(Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/auth/register";
        }
        if (user.getNickname() != null) {
            return "redirect:/user/center";
        }
        model.addAttribute("user", user);
        return "wAuth/register2";
    }

    //web注册页面3
    @RequestMapping(value = "register3", method = RequestMethod.GET)
    public String regist3Get(Model model) {
        UserDto user = getRequiredCurrentUser();
        if (user == null) {
            return "redirect:/auth/register";
        }
        if (user.getNickname() == null) {
            user.setNickname("用户" + user.getId());
            userService.edit(user);
            user = userService.getUserDto(user.getId());
            request.getSession().setAttribute(Const.SS_USER, user);
        }
//        model.addAttribute("user", user);
        return "wAuth/register3";
    }

    //web2资料修改
    @RequestMapping(value = "register2", method = RequestMethod.POST)
    public String register2post(String nickname, String address, String description) {
        UserDto user = getRequiredCurrentUser();
        user.setAddress(address);
        if (nickname == null || nickname == "") {
            nickname = "用户" + user.getId();
        }
        user.setNickname(nickname);
        user.setDescription(description);
        user.setPassword(null);
        userService.edit(user);

        user = userService.getUserDto(user.getId());
        request.getSession().setAttribute(Const.SS_USER, user);
        return "wAuth/register3";
    }

    // 发送短信验证码
    @ResponseBody
    @RequestMapping(value = "smsCode", method = RequestMethod.GET)
    public ResultOut smsCode(@RequestParam String mobile, @RequestParam byte type, @RequestParam(required = false) String validCode) {
        if (type != Const.SMS_REGIST && type != Const.SMS_RESETPWD && type != Const.SMS_FORGOTTPWD && type != Const.SMS_BINDACCOUNT) {
            throw new ServerException(HttpStatus.BAD_REQUEST);
        }
        if (type == Const.SMS_FORGOTTPWD)
            request.getSession().setAttribute("resetPwdMobileType", mobile + "mobile" + Const.SMS_RESETPASSWORD_TYPE_OK);//忘记密码验证标识
        if (StringUtils.isNotEmpty(validCode)) {     // 仅验证短信验证码是否有效
            validSmsService.validSmsCode(mobile, type, validCode, false);
        } else {
            UserDto user = userService.getByMobile(mobile);
            if (user != null)
                userService.validateUserState(user);
            if (user != null && type == Const.SMS_REGIST) {  // 注册
                throw new ServerException(HttpStatus.FORBIDDEN, M.E10007);
            } else if (user == null && type == Const.SMS_RESETPWD) { // 找回密码
                throw new ServerException(HttpStatus.FORBIDDEN, M.E10011);
            } else if (user == null && type == Const.SMS_FORGOTTPWD) { // 忘记密码
                throw new ServerException(HttpStatus.FORBIDDEN, M.E10011);
            }
            validSmsService.couSms(mobile, type);
        }
        return getResultOut(M.I10200.getCode());
    }

    //web登录
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginGet(Model model, @RequestParam(required = false) String mobile) {
        request.getSession().setAttribute("weixinState", "weixin_" + RandomStringUtils.randomAlphanumeric(12));
        request.getSession().setAttribute("qqState", "qq_" + RandomStringUtils.randomAlphanumeric(12));
        model.addAttribute("configer", configer);
        model.addAttribute("mobile", mobile);
        return "wAuth/login";
    }

    // 用户Ajax登录
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public User loginPost(@RequestParam String username, @RequestParam String password, @RequestParam String captcha, HttpServletResponse response) {
        String scaptcha = getSessionStringValue(Constants.KAPTCHA_SESSION_KEY);
        if (scaptcha == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10009);
        }
        if (!scaptcha.equalsIgnoreCase(captcha)) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10010);
        }
        return userService.doLogin(username, password, response);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletResponse response) {
        userService.doLogout(response);
        return "redirect:/auth/login";
    }

    //web忘记密码
    @RequestMapping(value = "forgotPwd", method = RequestMethod.GET)
    public String forgotPwd() {
        return "forgot-pwd";
    }


    //忘记密码  重置密码
    @RequestMapping(value = "resetPwd", method = RequestMethod.GET)
    public String smsResetPwd(Model model, @RequestParam String mobile) {
        model.addAttribute("mobile", mobile);
        return "reset-pwd";
    }

    //忘记密码  重置密码
    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    public String resetPwdPost(Model model, @RequestParam String password, @RequestParam String mobile) {
        if (!(mobile + "mobile" + Const.SMS_RESETPASSWORD_TYPE_OK).equals(request.getSession().getAttribute("resetPwdMobileType"))) {
            model.addAttribute("error", "请先进行手机短信验证！");
            return "reset-pwd";
        }
        userService.updateByMobile(mobile, password);
        model.addAttribute("messageSuccess", "密码修改成功,");
        model.addAttribute("messageLogin", "请登录");
        request.getSession().removeAttribute("resetPwdMobileType");
        return "reset-pwd";
    }


    //用户是否注册
    @RequestMapping(value = "checkUser", method = RequestMethod.POST)
    public String checkUser(Model model, @RequestParam String password, @RequestParam String mobile) {
        userService.updateByMobile(mobile, password);
        model.addAttribute("messageSuccess", "密码修改成功,");
        model.addAttribute("messageLogin", "请登录");
        return "reset-pwd";
    }

}
