package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.model.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author: dhc
 * Date: 2016-07-18 15:26
 */
@Controller
@RequestMapping("admin")
public class AdmAuthController extends AdmController {

    @ResponseBody
    @RequestMapping(value = "login")
    public Admin loginPost() {
        return getRequiredCurrentAdmin();
    }
}
