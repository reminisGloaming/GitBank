package com.doghome.easybuy.controller;


import com.doghome.easybuy.entity.User;
import com.doghome.easybuy.service.LoginService;
import com.doghome.easybuy.util.AjaxResult;
import com.doghome.easybuy.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    // 后面注销用户要用
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/doLogin")
    public AjaxResult login(@RequestBody User user) throws JsonProcessingException {
        Map<String, Object> params = loginService.login(user);

        return AjaxResult.success().add("isCorrect", "true")
                .add("token", params.get("token"));

    }


    @RequestMapping("/loginOut")
    public AjaxResult  loginOut(@RequestHeader(name="Authorization") String token){
        String loginName= JwtUtil.getLoginName(token);
        //如果redis有 loginName 这个键
        if(stringRedisTemplate.hasKey(loginName)){
            stringRedisTemplate.delete(loginName);
        }

        //返回成功的信息
        return AjaxResult.success().add("msg","true");
    }


}
