package com.doghome.easybuy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /*
     * to 收件人的邮箱地址
     * subject 邮件标题
     * text 邮件内容
     */
    public void sendSimpleMail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1259878347@qq.com");
        message.setTo(to);
        message.setSubject("易买网验证");
        message.setText(text);
        mailSender.send(message);
    }
}
