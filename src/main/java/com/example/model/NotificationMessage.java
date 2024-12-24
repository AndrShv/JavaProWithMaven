package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Entity
@Getter
@Setter
@Table(name = "messages")
public class NotificationMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "recipient")
    private String recipient;
    private String subject;
    private String text;

    public NotificationMessage(String recipient, String subject, String text) {
        this.recipient = this.recipient;
        this.subject = subject;
        this.text = text;
    }
    public NotificationMessage() {
    }
}
