package com.example.service;

import com.example.model.NotificationMessage;
import com.example.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service("emailNotificationService")
public class NotificationService {

    private final EmailService emailService;
    private final NotificationRepository notificationRepository;

    public NotificationService(EmailService emailService, NotificationRepository notificationRepository) {
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
    }

    public void sendCourseNotification(String recipientEmail) {
        String subject = "Course Notification: ";
        String text = "You have successfully registered for the course.";
        NotificationMessage message = new NotificationMessage(recipientEmail, subject, text);
        notificationRepository.save(message);
        emailService.sendToNotification(recipientEmail, subject, text);
    }
}


