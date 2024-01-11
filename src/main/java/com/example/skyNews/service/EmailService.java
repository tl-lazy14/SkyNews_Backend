package com.example.skyNews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lamit1bk1410@gmail.com");
        message.setTo(email);
        message.setSubject("Mã xác nhận đăng ký tài khoản SkyNews");
        message.setText("Mã xác nhận của bạn là: " + code);

        javaMailSender.send(message);
    }
}
