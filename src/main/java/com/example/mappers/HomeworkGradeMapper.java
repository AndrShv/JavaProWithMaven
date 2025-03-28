package com.example.mappers;

import com.example.dto.request.HomeworkGradeRequest;
import com.example.dto.response.HomeworkGradeResponse;
import com.example.model.HomeworkGrade;
import com.example.model.Homework;
import com.example.model.User;
import org.springframework.stereotype.Component;

@Component
public class HomeworkGradeMapper {

    public HomeworkGradeResponse toResponse(HomeworkGrade homeworkGrade) {
        HomeworkGradeResponse response = new HomeworkGradeResponse();
        Homework homework = homeworkGrade.getHomework();
        User teacher = homeworkGrade.getTeacher();
        User student = homeworkGrade.getStudent();
        response.setId(homeworkGrade.getId());
        response.setHomeworkId(homework.getId());
        response.setHomeworkTitle(homework.getTitle());
        response.setTeacherName(teacher != null ? teacher.getUsername() : "Unknown");
        response.setStudentName(student != null ? student.getUsername() : "Unknown");
        response.setGrade(homeworkGrade.getGrade());

        return response;
    }

    public HomeworkGrade toEntity(HomeworkGradeRequest request) {
        HomeworkGrade homeworkGrade = new HomeworkGrade();
        homeworkGrade.setGrade(request.getGrade());
        return homeworkGrade;
    }
}
