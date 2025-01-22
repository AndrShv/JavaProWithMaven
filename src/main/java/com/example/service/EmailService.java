package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender  = javaMailSender;
    }

    public void sendToNotification(String to, String subject, String text) {
        SimpleMailMessage  message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
        System.out.println("Email sent to: " + to + ", Subject: " + subject + ", Text: " + text);

        try{
            javaMailSender.send(message);
        }catch (Exception e){
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}
