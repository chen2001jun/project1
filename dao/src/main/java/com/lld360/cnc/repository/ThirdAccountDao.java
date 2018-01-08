package com.lld360.cnc.repository;

import com.lld360.cnc.model.ThirdAccount;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * ThirdAccount 数据库操作
 */
@Repository
public interface ThirdAccountDao{
	
    List<ThirdAccount> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    ThirdAccount find(Long id);

    long findByOpenidCount(String openid);

    ThirdAccount findByOpenid(String openid);

    void create(ThirdAccount thirdaccount);

    int update(ThirdAccount thirdaccount);

    void delete(Long id);

    long isBindThirdAccount( @Param("userId") long userId,@Param("type") byte type);

}