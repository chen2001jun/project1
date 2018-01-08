package com.lld360.cnc.admin.controller;

import com.lld360.cnc.admin.AdmController;
import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("admin/doc")
public class AdmDocContronller extends AdmController {
    @Autowired
    private DocService docService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Doc> docPageGet(@RequestParam(required = false) String sortBy,
                                @RequestParam(required = false) String sortType) {
        Map<String, Object> params = getParamsPageMap(15);
        boolean sort = sortBy != null && sortType != null && sortBy.matches("views|downloads") && sortType.matches("asc|desc");
        if (!sort) {
            params.remove("sortBy");
            params.remove("sortType");
        }
        return docService.search(params);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Doc docGet(@PathVariable long id) {
        return docService.find(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Doc docPost(@Valid @RequestBody Doc doc, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = bindResult2Map(result);
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setData(errors);
        } else {
            doc.setUploader(getRequiredCurrentAdmin().getId().longValue());
            doc.setUploaderType(Const.DOC_UPLOADERTYPE_ADMIN);
            docService.create(doc);
            return doc;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Doc docPut(@PathVariable long id, @Valid @RequestBody Doc doc, BindingResult result) {
        if (result.hasErrors()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setData(bindResult2Map(result));
        } else {
            doc.setId(id);
            return docService.update(doc) ? docService.find(id) : doc;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResultOut docDelete(@PathVariable long id) {
        Doc doc = docService.find(id);
        if (doc != null) {
            docService.delete(id);
            return getResultOut(M.I10200.getCode());
        }
        throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("文档不存在");
    }

    @RequestMapping(value = "{id}/doc", method = RequestMethod.POST)
    public Doc docFilePost(@PathVariable long id, @RequestParam MultipartFile file) {
        Doc doc = docService.find(id);
        try {
            docService.upload(file, doc);
        } catch (IOException e) {
            logger.warn("上传文档失败", e);
            throw new ServerException(HttpStatus.BAD_REQUEST, M.S90500).setMessage("上传失败: " + e.getMessage());
        }
        return doc;
    }

}
