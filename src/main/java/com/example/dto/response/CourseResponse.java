package com.example.dto.response;

import com.example.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private String teacherUsername;
    private String teacherEmail;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private String theme;
    private String way;


}
