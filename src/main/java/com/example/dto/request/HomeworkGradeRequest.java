package com.example.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeworkGradeRequest {
    private Long homeworkId;
    private Long teacherId;
    private Long studentId;
    private int grade;
}
