package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeworkGradeResponse {
    private Long id;
    private Long homeworkId;
    private String homeworkTitle;
    private String teacherName;
    private String studentName;
    private int grade;
}
