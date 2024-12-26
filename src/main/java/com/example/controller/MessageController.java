package com.example.controller;


import com.example.service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageSender messageSender;

    @Autowired
    public MessageController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @GetMapping("/send-message")
    public String sendMessage() {
        messageSender.sendMessage("Hello, RabbitMQ!");
        return "Message sent!";
    }
}
