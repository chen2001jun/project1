package com.lld360.cnc.website.controller;

import com.lld360.cnc.core.Const;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.vo.ResultOut;
import com.lld360.cnc.dto.DocDto;
import com.lld360.cnc.model.Doc;
import com.lld360.cnc.model.DocCategory;
import com.lld360.cnc.model.User;
import com.lld360.cnc.repository.DocCategoryDao;
import com.lld360.cnc.service.*;
import com.lld360.cnc.website.SiteController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Author: dhc
 * Date: 2016-08-04 17:09
 */
@Controller
@RequestMapping("doc")
public class DocController extends SiteController {

    private static final int DID_BASE = 54876;          // 文档基础码
    private static final int UID_BASE = 84756;          // 用户基础码
    private static final int DOWNTIME = 30;             // 下载有效时间

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

    @Autowired
    private DocCategoryService docCategoryService;

    //搜索
    @RequestMapping(value = "/searchrecord", method = {RequestMethod.GET, RequestMethod.POST})
    public String searchrecord(Model model) {
        model.addAttribute("docs", docService.searchWeb(getParamsPageMapTen()));
        Object content = getParamsPageMapTen().get("content");
        if (content != null) {
            searchWordsService.updateOrCreate(String.valueOf(content));
        }
        Map<String, Object> paramSearchWords = new HashMap<>();
        Map<String, Object> paramSearchCategory = new HashMap<>();

        paramSearchWords.put("limit", 5);
        paramSearchCategory.put("limit", null);

        model.addAttribute("searchWords", searchWordsService.getSearchingWords(paramSearchWords));
        model.addAttribute("searchCategory", docCategoryService.getSearchingFirstCategory(paramSearchCategory));

        return "doc-search";
    }


    // 获取二级分类
    @ResponseBody
    @RequestMapping(value = "SecondCategory", method = {RequestMethod.GET, RequestMethod.POST})
    public List<DocCategory> getSearchingSecondCategory(DocCategory docCategory) {

        //DocCategory docCategory=new DocCategory();
        //docCategory.setId(id);
        System.out.println("id:" + docCategory.getId());
        return docCategoryService.getSearchingSecondCategory(docCategory);

    }


    // 获取文库Ajax
    @ResponseBody
    @RequestMapping(value = "searchRecordAjax", method = {RequestMethod.GET, RequestMethod.POST})
    public List<Doc> searchRecordAjax(DocCategory docCategory, Integer page) {

        //docService.searchWeb(getParamsPageMap();
//        DocCategory docCategory=new DocCategory();
//        docCategory.setId(id);
        System.out.println("id:" + docCategory.getId());


        System.out.println("page:" + page);
        docCategory.setPage(page * 5);
        return docService.searchRecordAjax(docCategory);

    }


    // 获取文库分页信息Ajax
    @ResponseBody
    @RequestMapping(value = "searchRecordAjaxCount", method = {RequestMethod.GET, RequestMethod.POST})
    public List<Map<String, Integer>> searchRecordAjaxCount(DocCategory docCategory) {

        List<Map<String, Integer>> pagerList = new ArrayList<Map<String, Integer>>();

        Integer docCount=docService.searchRecordAjaxCount(docCategory);
        Integer totalPage=getTotalPage(docCount);
        System.out.println("totalPage:"+totalPage);


        for (int i = 0; i < totalPage; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("pager", i);
            map.put("id", docCategory.getId());
            map.put("totalCount", docCount);
            pagerList.add(map);
        }
        docCategory.setPage(docCount);
        return pagerList;

    }

    private Integer getTotalPage(Integer docCount){
        int page=0;
        if(docCount%5==0){
            page=docCount/5;
        }else{
            page=docCount/5+1;

        }

        return page;
    }


    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detailGet(@PathVariable long id, @RequestParam(required = false) Integer categoryId, Model model) {
        Doc doc = docService.findWeb(id);
        File docFile = new File(configer.getFileBasePath() + doc.getFilePath());
        model.addAttribute("isExists", docFile.exists());
        if (categoryId != null) model.addAttribute("category", docCategoryDao.find(categoryId));
        // 增加访问统计
        monthReportService.addDocViews();
        model.addAttribute("doc", doc);
        if (doc.getFileType().equals("txt")) {
            model.addAttribute("content", docService.getTxtContent(docFile, 2000));
        }
        model.addAttribute("images", docService.findByDocId(id));
        model.addAttribute("isCollect", getCurrentUser() == null ? "unLogin" : docCollectService.isCollect(getCurrentUser().getId(), id));

        //
        Map<String, Object> params = getParamsPageMap(6);
        params.put("content", doc.getTitle());
        Page<Doc> docs = docService.searchWeb(params);
        List<Doc> docList = docs.getContent();
        if (docs.getContent().isEmpty()) {
            params.put("fileType", doc.getFileType());
            params.put("sortBy", "views");
            docList = docService.searchForIndex(params);
        }
        model.addAttribute("recommendList", docList);

        Map<String, Object> paramForHot = getParamsPageMap(3);
        paramForHot.put("sortBy", "views");
        model.addAttribute("hotDocs", docService.searchForIndex(paramForHot));  //热门文当推荐

        return "doc-detail";
    }

    // 加密ID
    private String enDownloadCode(long userId, long docId) {
        String dCode = Long.toString(docId + DID_BASE, Character.MAX_RADIX);
        String uCode = Long.toString(userId + UID_BASE, Character.MAX_RADIX);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, DOWNTIME);
        String timeCode = Long.toString(calendar.getTimeInMillis(), Character.MAX_RADIX);
        return StringUtils.reverse(uCode + "-" + dCode + "-" + timeCode);
    }

    // 解密ID
    private long deDownloadCode(String s) {
        String[] args = StringUtils.reverse(s).split("-");
        if (args.length == 3) {
            try {
                long time = Long.parseLong(args[2], Character.MAX_RADIX);
                if (time >= Calendar.getInstance().getTimeInMillis()) {
                    long uid = Long.parseLong(args[0], Character.MAX_RADIX) - UID_BASE;
                    if (uid == getRequiredCurrentUser().getId())
                        return Long.parseLong(args[1], Character.MAX_RADIX) - DID_BASE;
                }
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    // 获取下载代码
    @RequestMapping(value = "/dl/{id}", method = RequestMethod.GET)
    public ResponseEntity<ResultOut> dlCodeGet(@PathVariable long id) {
        Doc doc = docService.get(id);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        User user = getRequiredCurrentUser();
        docService.downloadRecord(user, doc);
        // 增加下载统计
        monthReportService.addDocDownloads();
        return new ResponseEntity<>(getResultOut(M.I10200.getCode())
                .setData(enDownloadCode(getRequiredCurrentUser().getId(), id)), HttpStatus.OK);
    }

    // 下载文件
    @RequestMapping(value = "/dl", method = RequestMethod.GET)
    public ResponseEntity<byte[]> docFileGet(@RequestParam String c, HttpServletResponse response) throws IOException {
        long id = deDownloadCode(c);
        if (id == 0) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        Doc doc = docService.find(id);
        if (doc == null) {
            throw new ServerException(HttpStatus.NOT_FOUND, M.S90404);
        }
        File file = docService.download(getRequiredCurrentUser(), doc);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLengthLong(file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(doc.getFileName().getBytes("utf-8"), "iso8859-1"));
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), HttpStatus.OK);
    }

    @RequestMapping(value = "/collect", method = RequestMethod.GET)
    public ResponseEntity collectGet(@RequestParam long id) {
        docCollectService.docCollect(getRequiredCurrentUser().getId(), id);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 取消收藏
    @RequestMapping(value = "/uncollect", method = RequestMethod.GET)
    public ResponseEntity uncollectPost(long id) {
        docCollectService.uncollect(getRequiredCurrentUser().getId(), id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //Author: yangb
    @RequestMapping(value = "myDownload", method = RequestMethod.GET)
    public String myDownload(Model model) {
        User user = getRequiredCurrentUser();
        List<DocDto> myDownloads = docService.getDownloads(user.getId());
        model.addAttribute("docs", myDownloads);
        return "wDoc/doc-mydownload";
    }

    @RequestMapping(value = "myUpload", method = RequestMethod.GET)
    public String myUpload(Model model) {
        User user = getRequiredCurrentUser();
        Map<String, Object> param = new HashMap<>();
        param.put("uploader", user.getId());
        param.put("unstate", 9);
        List<Doc> myUploads = docService.getUploads(param);
        model.addAttribute("docs", myUploads);
        return "wDoc/doc-myupload";
    }

    @RequestMapping(value = "myFav", method = RequestMethod.GET)
    public String myFav(Model model) {
        User user = getRequiredCurrentUser();
        List<DocDto> myFavs = docService.getCollects(user.getId());
        model.addAttribute("docs", myFavs);
        return "wDoc/doc-myfav";
    }

    @RequestMapping(value = "upload", method = RequestMethod.GET)
    public String docUploadGet() {
        return "wDoc/doc-upload";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String docUploadPost(@Valid Doc doc, BindingResult result, MultipartFile file, Model model) {
        if (file == null) {
            model.addAttribute("error", "必须指定上传文件");
            return "redirect:/doc/upload";
        }
        if (result.hasErrors()) {
            model.addAttribute("error", String.join("<br>", bindResult2Map(result).values()));
        } else {
            User user = getRequiredCurrentUser();
            doc.setUploader(user.getId());
            doc.setUploaderType(Const.DOC_UPLOADERTYPE_USER);
            try {
                docService.uploadWithInfo(file, doc);
                return "redirect:/doc/myUpload";
            } catch (ServerException e) {
                model.addAttribute("error", getMessage(e.getCode().toString()));
            } catch (IOException e) {
                logger.error("用户上传文档出错", e);
                model.addAttribute("error", "文件上传错误");
            }
        }
        return "wDoc/doc-upload";
    }

    //批量取消收藏
    @RequestMapping(value = "unConllect", method = RequestMethod.POST)
    public String doUnconllect() {
        String[] myconllects = request.getParameterValues("myconllects");
        for (String myConllectId : myconllects) {
            long myconlelctid = Long.parseLong(myConllectId);
            uncollectPost(myconlelctid);
        }
        return "redirect:/doc/myFav";
    }

    //逻辑删除已上传
    @RequestMapping(value = "docDelete", method = RequestMethod.POST)
    public String docDelete() {
        String[] docIds = request.getParameterValues("docIds");
        for (String docId : docIds) {
            long id = Long.parseLong(docId);
            Doc doc = docService.find(id);
            if (doc == null) {
                throw new ServerException(HttpStatus.BAD_REQUEST, M.S90400).setMessage("文档不存在");
            }
            docService.delete(id);
        }
        return "redirect:/doc/myUpload";
    }
}
