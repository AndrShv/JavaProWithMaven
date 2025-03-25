package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class HomeworkResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime doneAtTime;
    private Long lessonId;
    private int grade;
    private String comment;
}
