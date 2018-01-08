package com.lld360.cnc.repository;

import com.lld360.cnc.model.Setting;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingDao {

    void insert(Setting setting);

    Setting find(String code);

    int update(Setting setting);
}