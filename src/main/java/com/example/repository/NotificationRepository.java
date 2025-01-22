package com.example.repository;

import com.example.model.NotificationMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationMessage, Long> {
    List<NotificationMessage> findByUserIdAndIsRead(String userId, Boolean isRead);
}

