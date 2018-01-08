package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.model.Admin;
import com.lld360.cnc.repository.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: dhc
 * Date: 2016-07-13 14:15
 */
@Service
public class AdminService extends BaseService {

    @Autowired
    AdminDao adminDao;

    public Admin get(String account) {
        return adminDao.findByAccount(account);
    }

    public int update(Admin admin) {
        return adminDao.update(admin);
    }
}
