package com.lld360.cnc.admin.controller;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.model.CncDaysStatistics;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.service.DocReportService;
import com.lld360.cnc.service.MonthReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by WW on 2016/8/17.
 */
@RestController
@RequestMapping("admin/monthreport")
public class AdmMonthReportController extends BaseController {
    @Autowired
    public MonthReportService monthReportService;

    //获取阅读下载统计查询月报表信息
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<CncDaysStatistics> reportPageGet() {
        return monthReportService.search(getParamsPageMap(15));
    }

    //获取阅读下载总量
    @RequestMapping(value = "totalSum", method = RequestMethod.GET)
    public Page<CncDaysStatistics> totalSumGet() {
        return monthReportService.totalSumsearch(getParamsPageMap(15));
    }
}
