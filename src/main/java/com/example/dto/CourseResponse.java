package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private String teacherUsername;
    private LocalDateTime createdAt;
}
