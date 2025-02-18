package com.example.controller.course;

import com.example.model.Homework.Homework;
import com.example.service.homework.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homeworks")
@RequiredArgsConstructor
public class HomeworkController {
    private final HomeworkService homeworkService;

    @PostMapping("/course/{courseId}")
    public ResponseEntity<Homework> createHomework(@PathVariable Long courseId, @RequestBody Homework request) {
        Homework homework = homeworkService.createHomework(courseId, request.getDescription(), request.getDeadline(), request.isAutoCheck(), request.getMaxScore());
        return ResponseEntity.ok(homework);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Homework>> getHomeworks(@PathVariable Long courseId) {
        return ResponseEntity.ok(homeworkService.getAllHomeworksFromCourse(courseId));
    }
}