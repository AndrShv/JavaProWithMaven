package com.example.model;

import com.example.extraConfigs.HomeworkStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "homeworks")
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HomeworkStatus status = HomeworkStatus.IN_PROGRESS;

    @Column(nullable = false)
    private LocalDateTime doneAtTime;

    @Column(nullable = false)
    private int countingTries;

    @PrePersist
    protected void onCreate() {
        this.doneAtTime = LocalDateTime.now();

    }
}
