package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.dto.DocDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.DocService;
import com.lld360.cnc.service.UserService;
import com.lld360.cnc.website.SiteController;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("m/user")
public class MUserController extends SiteController {

    @Autowired
    private UserService userService;

    @Autowired
    private DocService docService;


    @RequestMapping(value = "/center", method = RequestMethod.GET)
    public String centerGet(Model model) {
        User user = getCurrentUser();
        if (user != null) {
            model.addAttribute("user", user);
            if (user.getId() != null) {
                model.addAttribute("collects", docService.getCollects(user.getId()));
                model.addAttribute("userScore", userService.findUserScore(user.getId()));
            }
        }
        return "m/user-center";
    }

    @ResponseBody
    @RequestMapping(value = "/myCollects", method = RequestMethod.GET)
    public List<DocDto> getCollectsGet() {
        return docService.getCollects(getRequiredCurrentUser().getId());
    }

    @ResponseBody
    @RequestMapping(value = "/myDownloads", method = RequestMethod.GET)
    public List<DocDto> getDownloadsGet() {
        return docService.getDownloads(getRequiredCurrentUser().getId());
    }

    //前往用户功能列表页面
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listGet() {
        return "m/user-list";
    }

    // 修改密码
    @RequestMapping("repwd")
    public String rePwdGet(Model model, HttpServletResponse response) {
        if (request.getMethod().equalsIgnoreCase("POST")) {
            String op = request.getParameter("op");
            if (StringUtils.isEmpty(op)) {
                model.addAttribute("error", "原密码不能为空");
                return "m/user-repwd";
            }
            String np = request.getParameter("np");
            if (StringUtils.isEmpty(np)) {
                model.addAttribute("error", "新密码不能为空");
                return "m/user-repwd";
            }
            User user = userService.getUser(getRequiredCurrentUser().getId());
            if (!user.getPassword().equals(DigestUtils.md5Hex(StringUtils.reverse(op) + user.getMobile()))) {
                model.addAttribute("error", "原密码错误");
                return "m/user-repwd";
            }
            user.setPassword(DigestUtils.md5Hex(StringUtils.reverse(np) + user.getMobile()));
            userService.updateUser(user);
            userService.doLogout(response);
            model.addAttribute("success", "修改成功");
        }
        return "m/user-repwd";
    }
}
