package com.lld360.cnc.repository;

import com.lld360.cnc.dto.UserDto;
import com.lld360.cnc.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {

    List<UserDto> search(Map<String, Object> parameters);

    long count(Map<String, Object> parameters);

    User find(Long id);

    UserDto findByMobile(String mobile);

    void create(UserDto user);

    int update(User user);

    int updateByMobile(User user);

    int updateState(@Param("userId") long userId, @Param("state") byte state);

    int updateAvatar(@Param("id") long id, @Param("relativeFile") String relativeFile);

    UserDto findUserDto(long id);
}