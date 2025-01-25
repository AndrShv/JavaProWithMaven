package com.example.controller.notifications;


import com.example.dto.request.EmailRequest;
import com.example.service.notifications.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final EmailService emailService;

    @Autowired
    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendNotification(emailRequest.getRecipient(), emailRequest.getSubject(), emailRequest.getMessage());
        return ResponseEntity.ok("Email sent successfully");
    }
}
