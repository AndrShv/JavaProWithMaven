package com.example.rabbitMqConfigs;

import com.example.configuration.messaging.RabbitMQConfig;
import com.example.model.NotificationMessage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(NotificationMessage message) {
        notificationService.sendNotification(message.getRecipient(), message.getSubject(), message.getText());
    }
}