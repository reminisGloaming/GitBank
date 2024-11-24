package com.doghome.easybuy.service.impl;

import com.antherd.smcrypto.sm3.Sm3;
import com.doghome.easybuy.entity.User;
import com.doghome.easybuy.mapper.UserMapper;
import com.doghome.easybuy.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectUserByLoginName(String loginName) {

        return userMapper.selectUserByLoginName(loginName);
    }


    @Override
    public PageInfo<User> UserByPage(Map<String, Object> params) {
        params.put("orderBy", "id");

        String userName = "";

        if (params.containsKey("userName") && !StringUtils.isNullOrEmpty(params.get("userName").toString())) {
            userName = "%" + params.get("userName").toString() + "%";
        }
        params.put("userName", userName);

        String createTime = "";

        if (params.containsKey("createTime") && !StringUtils.isNullOrEmpty(params.get("createTime").toString())) {
            createTime = "%" + params.get("createTime").toString() + "%";
        }
        params.put("createTime", createTime);

        PageHelper.startPage(params);
        List<User> userList = userMapper.UserByPage(params);
        return new PageInfo<>(userList, 3);
    }

    @Override
    public boolean addUser(Map<String, Object> params) {
        //把密码进行加密
        String password = params.get("password").toString();
        String sm3password = Sm3.sm3(password);
        params.put("password", sm3password);
        return userMapper.addUser(params) > 0;
    }

    @Override
    public User selectUserById(int id) {
        return userMapper.selectUserById(id);
    }

    @Override
    public boolean updateUser(Map<String, Object> params) {
        //把密码进行加密

        if (params.containsKey("password") && !StringUtils.isNullOrEmpty(params.get("password").toString())) {
            String password = params.get("password").toString();
            String sm3password = Sm3.sm3(password);
            params.put("password", sm3password);
        }

        return userMapper.updateUser(params) > 0;
    }


    @Override
    public boolean deleteUser(int id) {
        return userMapper.deleteUser(id) > 0;
    }

}
