package com.example.controller;

import com.example.dto.LessonRequest;
import com.example.dto.LessonResponse;
import com.example.model.Lesson;
import com.example.service.LessonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;

    }
    @PostMapping("/{courseId}")
    public LessonResponse createLesson(@RequestBody LessonRequest request, @PathVariable Long courseId){
        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        return lessonService.createLesson(courseId, lesson);
    }

    @GetMapping("/{id}")
    public LessonResponse getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id);
    }
}
