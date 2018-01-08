package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.model.Setting;
import com.lld360.cnc.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Author: dhc
 * Date: 2016-08-08 15:46
 */
@RestController
@RequestMapping("admin/setting")
public class AdmSettingController extends AdmController {
    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Setting settingPost(@Valid @RequestBody Setting setting) {
        return settingService.save(setting);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Setting settingGet(@RequestParam String code) {
        return settingService.get(code);
    }
}
