package com.example.rabbitMqConfigs;

import com.example.configuration.messaging.RabbitMQConfig;
import com.example.model.NotificationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;


@Service
public class NotificationService {
    @Autowired
    private RabbitTemplate  rabbitTemplate;

    @Autowired
    private JavaMailSender  javaMailSender;

    public void sendNotification(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); message.setTo(to); message.setSubject(subject);
        message.setText(String.valueOf(text));
        javaMailSender.send(message);
    }

    public void sendAsyncNotification(String to, String subject, String text){
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, new NotificationMessage(to, subject, text));
    }
}