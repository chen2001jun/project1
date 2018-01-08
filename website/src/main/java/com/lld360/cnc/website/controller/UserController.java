package com.lld360.cnc.website.controller;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.model.UserScore;
import com.lld360.cnc.service.*;
import com.lld360.cnc.website.SiteController;
import com.lld360.cnc.website.service.ThirdAccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends SiteController {

    @Autowired
    private UserService userService;

    @Autowired
    private DocService docService;

    @Autowired
    private DocCollectService docCollectService;

    @Autowired
    private FileUtilService fileUtilService;

    @Autowired
    private ValidSmsService validSmsService;

    @Autowired
    private ThirdAccountService thirdAccountService;

    //用户注册时上传头像
    @ResponseBody
    @RequestMapping(value = "uploadAvatar", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User uploadAvatar(MultipartFile file,
                             @RequestParam Double x,
                             @RequestParam Double y,
                             @RequestParam Double w,
                             @RequestParam Double h) {
        if (file == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("文件不存在");
        }
        Rectangle rectangle = new Rectangle(x.intValue(), y.intValue(), w.intValue(), h.intValue());
        String fileName = fileUtilService.cutFile(file, rectangle);

        UserDto currentUser = getRequiredCurrentUser();
        String avatar = fileUtilService.moveUploadedFile(fileName, Const.FILE_USER, currentUser.getId(), currentUser.getAvatar());

        User user = new User();
        user.setId(currentUser.getId());
        user.setAvatar(avatar);
        userService.updateUser(user);
        currentUser.setAvatar(avatar);
        request.getSession().setAttribute(Const.SS_USER, currentUser);
        return user;
    }

    // 获取用户积分
    @ResponseBody
    @RequestMapping(value = "score", method = RequestMethod.GET)
    public UserScore userScoreGet() {
        UserDto userDto = getCurrentUser();
        if (userDto != null && userDto.getId() != null) {
            return userService.findUserScore(userDto.getId());
        }
        return null;
    }

    //web用户中心
    @RequestMapping(value = "center", method = RequestMethod.GET)
    public String userCenter(Model model) {
        User user = getRequiredCurrentUser();
        Map<String, Object> params = new HashMap<>();
        long myDownloadCount = 0, myUploadCount = 0, myConllectCount = 0;

        if (user.getId() != null) {
            params.put("userId", user.getId());
            params.put("uploader", user.getId());
            params.put("uploaderType", Const.DOC_UPLOADERTYPE_USER);
            myDownloadCount = docService.getDownloadCount(params);
            myUploadCount = docService.countWeb(params);
            myConllectCount = docCollectService.conllectCount(params);
        }
        model.addAttribute("user", user);
        model.addAttribute("myDownloadCount", myDownloadCount);
        model.addAttribute("myUploadCount", myUploadCount);
        model.addAttribute("myConllectCount", myConllectCount);
        return "user-center";
    }

    //web用户编辑
    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public String userProfile(Model model) {
        User user = getRequiredCurrentUser();
        model.addAttribute("user", user);
        return "user-profile";
    }

    //个人用户信息修改
    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public String updateUser(String nickname, String address, String description, RedirectAttributes attributes) {
        UserDto user = getRequiredCurrentUser();
        if (nickname == null || nickname == "") {
            attributes.addFlashAttribute("errMsg", "昵称不能为空");
            return "redirect:/user/profile";
        }
        user.setNickname(nickname);
        user.setAddress(address);
        user.setDescription(description);
        user.setPassword(null);
        userService.edit(user);

        user = userService.getUserDto(user.getId());
        request.getSession().setAttribute(Const.SS_USER, user);
        attributes.addFlashAttribute("message", "修改成功");
        return "redirect:/user/profile";
    }

    //web修改密码
    @RequestMapping(value = "pwd", method = RequestMethod.GET)
    public String userPwd() {
        return "user-pwd";
    }

    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    public String updatePwd(RedirectAttributes attributes, @RequestParam String password, @RequestParam String newpassword, HttpServletResponse response) {
        User user = getRequiredCurrentUser();
        if (!user.getPassword().equals(DigestUtils.md5Hex(StringUtils.reverse(password) + user.getMobile()))) {
            attributes.addFlashAttribute("errMsg", "原密码错误");
            return "redirect:/user/pwd";
//            throw new ServerException(HttpStatus.NOT_ACCEPTABLE, M.E10002);
        }
        if (!newpassword.matches("\\w{6,20}")) {
//            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10002);
            attributes.addFlashAttribute("errMsg", "新建密码不符合规则");
            return "redirect:/user/pwd";
        }
        user.setPassword(newpassword);
        userService.edit(user);
        attributes.addFlashAttribute("message", "密码修改成功,请重新登录");
        request.setAttribute("mobile",user.getMobile());
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
    public String resetPwdGet(Model model, @RequestParam String mobile) {
        model.addAttribute("mobile", mobile);
        return "reset-pwd";
    }

    //忘记密码  重置密码
    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    public String resetPwdPost(Model model, @RequestParam String password, @RequestParam String mobile) {
        if (userService.getByMobile(mobile) == null) {
            model.addAttribute("message", "重置密码成功,请登录");
            return "wAuth/login";
        }
        userService.updateByMobile(mobile, password);
        model.addAttribute("message", "重置密码成功,请登录");
        return "wAuth/login";
    }

    //绑定手机号
    @ResponseBody
    @RequestMapping(value = "bindAccount", method = RequestMethod.POST)
    public String bindAccount(@RequestParam String password, @RequestParam String openid, @RequestParam String code, @RequestParam String mobile, @RequestParam byte bindType, @RequestParam byte smsType) {
        String message;
        validSmsService.validSmsCode(mobile, smsType, code);
        thirdAccountService.isBindThirdAccount(mobile, bindType);
        try {
            thirdAccountService.bindThirdAccount(openid, mobile, password);
        } catch (Exception e) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10017);
        }
        message = "绑定手机号成功！";
        return message;
    }
}
