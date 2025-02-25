package com.example.controller.course;

import com.example.model.Homework;
import com.example.model.Lesson;
import com.example.service.studying.LessonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
@PreAuthorize("hasRole('TEACHER')")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    // Создание урока
    @PostMapping("/{courseId}")
    public Lesson createLesson(@RequestBody Lesson lesson, @PathVariable Long courseId) {
        return lessonService.createLesson(courseId, lesson);
    }

    // Получение урока
    @GetMapping("/{id}")
    public Lesson getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id);
    }

    // Создание домашнего задания для урока
    @PostMapping("/{lessonId}/homework")
    public Homework createHomework(@PathVariable Long lessonId, @RequestBody Homework homework) {
        return lessonService.createHomework(lessonId, homework);
    }

    // Оценка домашнего задания
    @PutMapping("/{lessonId}/homework/{homeworkId}/grade")

    public Homework gradeHomework(@PathVariable Long homeworkId, @RequestParam int grade) {
        return lessonService.gradeHomework(homeworkId, grade);
    }

    // Добавление комментария к домашнему заданию
    @PutMapping("/{lessonId}/homework/{homeworkId}/comment")
    public Homework commentHomework(@PathVariable Long homeworkId, @RequestParam String comment) {
        return lessonService.commentHomework(homeworkId, comment);
    }
}
