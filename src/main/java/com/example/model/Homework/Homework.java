package com.example.model.Homework;

import com.example.courseDetails.HomeworkStatus;
import com.example.courseDetails.SubmissionStatus;
import com.example.model.Studing.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "homeworks")
public class Homework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private boolean autoCheck;

    @Column(nullable = false)
    private int maxScore;

    @Enumerated(EnumType.STRING)
    private HomeworkStatus status;

    @OneToMany(mappedBy = "homework")
    private List<SubmissionStatus> submissions;
}

