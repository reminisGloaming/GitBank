package com.doghome.easybuy.controller;

import com.doghome.easybuy.service.EmailService;
import com.doghome.easybuy.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送邮箱, 调用service 和 redis
     */
    @RequestMapping("/sendEmail")
    public AjaxResult sendEmail(String email) {
        String proveCode = (int) (Math.random() * 10000) + "";

        //发送邮箱  地址和验证码
        emailService.sendSimpleMail(email, proveCode);

        //redis 保存数据
        stringRedisTemplate.opsForValue().set(email, proveCode, 3, TimeUnit.MINUTES);

        return AjaxResult.success().add("proveCode", proveCode);
    }

    /**
     * 获取验证码 调用redis 获得验证码
     *
     * @param email
     * @return
     */

    @RequestMapping("/getProveCode")
    public AjaxResult getProveCode(String email) {

        String proveCode = stringRedisTemplate.opsForValue().get(email);

        return AjaxResult.success().add("proveCode", proveCode);
    }
}
