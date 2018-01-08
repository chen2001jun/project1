package com.lld360.cnc.admin.controller;

import com.lld360.cnc.base.BaseController;
import com.lld360.cnc.model.SearchWords;
import com.lld360.cnc.service.SearchWordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/searchword")
public class AdmSearchWordsController extends BaseController {
    @Autowired
    public SearchWordsService searchWordsService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<SearchWords> wordPageGet() {
        return searchWordsService.getSearchingWords(getParamsPageMap(15));
    }


    @RequestMapping(value = "doBatchClearZero", method = RequestMethod.GET)
    public boolean doBatchClearZero(@RequestParam String idsStr) {

        System.out.println("idsStr:"+idsStr);
        if(idsStr.startsWith(",")){
            idsStr=idsStr.substring(1,idsStr.length());
        }
        System.out.println("idsStr2:"+idsStr);


        String[] idsStrStringArray = idsStr.split(",");
        Integer[] idsStrIntegerArray = new Integer[idsStrStringArray.length];
        for (int i = 0; i < idsStrStringArray.length; i++) {
            idsStrIntegerArray[i] = Integer.valueOf(idsStrStringArray[i]);
        }

        return searchWordsService.doBatchClearZero(idsStrIntegerArray);
    }


    @RequestMapping(value = "doOneClearZero/{id}", method = RequestMethod.GET)
    public boolean doOneClearZero(@PathVariable Integer id) {

        System.out.println("id:"+id);
        return searchWordsService.doOneClearZero(id);
    }


}
