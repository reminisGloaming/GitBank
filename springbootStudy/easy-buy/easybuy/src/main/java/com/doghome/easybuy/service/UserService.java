package com.doghome.easybuy.service;

import com.doghome.easybuy.entity.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface UserService {

    User selectUserByLoginName(String loginName);

    PageInfo<User> UserByPage(Map<String,Object> params);


    boolean addUser(Map<String,Object>params);

    User selectUserById(int id);

    boolean updateUser(Map<String,Object>params);

    boolean deleteUser(int id);
}
