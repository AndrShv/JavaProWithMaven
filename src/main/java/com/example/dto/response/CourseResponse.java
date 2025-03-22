package com.example.dto.response;


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
    private String teacherEmail;
    private LocalDateTime startedTime;
    private LocalDateTime createdAt;
    private LocalDateTime finishedTime;
    private String theme;
    private String way;


}
