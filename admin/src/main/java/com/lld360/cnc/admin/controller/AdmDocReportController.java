package com.lld360.cnc.admin.controller;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.SearchWords;
import com.lld360.cnc.service.DocReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by WW on 2016/8/17.
 */
@RestController
@RequestMapping("admin/docreport")
public class AdmDocReportController extends BaseController {
    @Autowired
    public DocReportService docReportService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Doc> wordPageGet() {
        return docReportService.search(getParamsPageMap(15));
    }

    @RequestMapping(value = "/downloads", method = RequestMethod.GET)
    public Long countDownloads() {
        return docReportService.countDownloads(getParamsMap());
    }
}
