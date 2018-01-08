package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by WW on 2016/8/22.
 */
@Service
public class UserReportService extends BaseService {

    @Autowired
    private UserService userService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Page<UserDto> search(Map<String, Object> params) {
        Object beginTime = params.get("beginTime");
        if (beginTime != null) {
            try {
                Date begin = format.parse(String.valueOf(beginTime));
            } catch (ParseException e) {
                params.remove("beginTime");
            }
        }
        Object endTime = params.get("endTime");
        if (endTime != null) {
            try {
                Date end = format.parse(String.valueOf(endTime));
            } catch (ParseException e) {
                params.remove("endTime");
            }
        }
        return userService.getUsersByPage(params);
    }
}
