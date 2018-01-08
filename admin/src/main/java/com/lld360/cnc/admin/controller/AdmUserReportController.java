package com.lld360.cnc.admin.controller;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import com.lld360.cnc.service.UserReportService;
import com.lld360.cnc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by WW on 2016/8/22.
 */
@RestController
@RequestMapping("admin/userreport")
public class AdmUserReportController extends BaseController {

    @Autowired
    private UserReportService userReportService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<UserDto> wordPageGet() {
        return userReportService.search(getParamsPageMap(15));
    }
}
