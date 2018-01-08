package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.service.DocCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/docCategory")
public class AdmDocCategoryContronller extends AdmController {

    @Autowired
    private DocCategoryService docCategoryService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<DocCategory> docTagPageGet() {
        return docCategoryService.getAllCategory();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public DocCategory docCategoryPost(@Valid DocCategory category, @RequestParam(required = false) MultipartFile file) {
        docCategoryService.save(category, file);
        return docCategoryService.get(category.getId());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResultOut docCategoryDelete(@PathVariable int id) {
        docCategoryService.delete(id);
        return getResultOut(M.I10200.getCode());
    }

}
