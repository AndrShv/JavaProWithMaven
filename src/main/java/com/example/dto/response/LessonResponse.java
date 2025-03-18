package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonResponse {
    private Long id;
    private String title;
    private String content;
    private Long courseId;

    public LessonResponse(Long id, String title, String content, Long courseId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.courseId = courseId;
    }
}
