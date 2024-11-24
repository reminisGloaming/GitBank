package com.doghome.easybuy.service.impl;

import com.antherd.smcrypto.sm3.Sm3;
import com.doghome.easybuy.entity.User;
import com.doghome.easybuy.exception.NameAndPasswordException;
import com.doghome.easybuy.mapper.UserMapper;
import com.doghome.easybuy.service.LoginService;
import com.doghome.easybuy.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service

public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Map<String, Object> login(User user) {
        User loginUser = userMapper.selectUserByLoginName(user.getLoginName());


        //创建一个map储存Token
        Map<String, Object> params = new HashMap<>();

        //用户不存在
        if (loginUser == null) {
            throw new NameAndPasswordException("userNotExist");
        }


        //密码错误
        String password = Sm3.sm3(user.getPassword());

        if (password.equals(loginUser.getPassword())) {

            //调用JWT工具类生成Token令牌
            String token = JwtUtil.createToken(loginUser.getLoginName(),loginUser.getType());

            //redis存放令牌 ( 这里要开linux的redis服务器 )
            stringRedisTemplate.opsForValue().set(loginUser.getLoginName(), token, 60, TimeUnit.MINUTES);

            params.put("token", token);
        } else {
            throw new NameAndPasswordException("passwordError");
        }


        return params;
    }
}
