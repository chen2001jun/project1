package com.lld360.cnc.repository;

import com.lld360.cnc.model.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao {

    Admin findByAccount(String account);

    int update(Admin admin);

}