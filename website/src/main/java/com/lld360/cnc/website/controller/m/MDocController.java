package com.lld360.cnc.website.controller.m;

import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.repository.DocCategoryDao;
import com.lld360.cnc.service.DocCollectService;
import com.lld360.cnc.service.DocService;
import com.lld360.cnc.service.MonthReportService;
import com.lld360.cnc.service.SearchWordsService;
import com.lld360.cnc.website.SiteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: dhc
 * Date: 2016-08-04 17:09
 */
@Controller
@RequestMapping("m/doc")
public class MDocController extends SiteController {

    @Autowired
    private DocService docService;

    @Autowired
    private DocCollectService docCollectService;

    @Autowired
    private DocCategoryDao docCategoryDao;

    @Autowired
    private SearchWordsService searchWordsService;

    @Autowired
    private MonthReportService monthReportService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String searchrecord(Model model) {
        model.addAttribute("docs", docService.searchWeb(getParamsPageMap()));
        Object content = getParamsPageMap().get("content");
        if (content != null) {
            searchWordsService.updateOrCreate(String.valueOf(content));
        }
        Map<String, Object> param2 = new HashMap<>();
        param2.put("limit", 5);
        model.addAttribute("searchWords", searchWordsService.getSearchingWords(param2));
        return "m/doc-search-record";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String detailGet(@PathVariable long id, @RequestParam(required = false) Integer categoryId, Model model) {
        Doc doc = docService.findWeb(id);
        File docFile = new File(configer.getFileBasePath() + doc.getFilePath());
        model.addAttribute("isExists", docFile.exists());
        if (categoryId != null) model.addAttribute("category", docCategoryDao.find(categoryId));
        // 增加文档访问统计
        monthReportService.addDocViews();
        model.addAttribute("doc", doc);
        if (doc.getFileType().equals("txt")) {
            model.addAttribute("content", docService.getTxtContent(docFile, 2000));
        }
        model.addAttribute("images", docService.findByDocId(id));
        model.addAttribute("isCollect", getCurrentUser() == null ? "unLogin" : docCollectService.isCollect(getCurrentUser().getId(), id));

        return "m/doc-detail";
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String category(Model model) {
        model.addAttribute("categorys", docCategoryDao.getAll());
        return "m/category";
    }

    @RequestMapping(value = "/category/{c1}", method = RequestMethod.GET)
    public String categoryDoc(@PathVariable int c1, Model model) {
        DocCategory category = docCategoryDao.find(c1);
        if(category != null) {
            model.addAttribute("category", category);
            Map<String, Object> params = getParamsPageMap();
            params.put("hotType", "hotType");
            params.put("c1", c1);
            model.addAttribute("docs", docService.searchWeb(params));
            return "m/category-doc";
        }
        return "redirect:/m/doc/category";
    }
}
