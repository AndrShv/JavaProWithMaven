package com.example.controller.teacher;

import com.example.model.User;
import com.example.service.teacher.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    // Получить студентов по курсу
    @GetMapping("/courses/{courseId}/students")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<User>> getStudentsByCourse(@PathVariable Long courseId) {
        List<User> students = teacherService.getStudentsByCourse(courseId);
        return ResponseEntity.ok(students);
    }

    // Получить студентов по имени в курсе
    @GetMapping("/courses/{courseId}/students/search")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<User>> getStudentsByCourseAndName(
            @PathVariable Long courseId,
            @RequestParam String studentName) {
        List<User> students = teacherService.getStudentsByCourseAndName(courseId, studentName);
        return ResponseEntity.ok(students);
    }
}
