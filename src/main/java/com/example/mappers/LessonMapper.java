package com.example.mappers;

import com.example.dto.response.LessonResponse;
import com.example.model.Studing.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {
    public LessonResponse toResponse(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getContent(),
                lesson.getCourse().getId()
        );
    }
}
