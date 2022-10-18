package com.mybatis.mapper;

import com.mybatis.pojo.User;

import java.util.List;

public interface UserMapper {
    int insertUser();
    void updateUser();
    void deleteUser();
    User getUserById();
    List<User> getAllUser();
}
