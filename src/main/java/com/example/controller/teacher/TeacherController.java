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

    @GetMapping("/courses/{courseId}/students")
    @PreAuthorize("hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getStudentsByCourse(@PathVariable Long courseId) {
        List<User> students = teacherService.getStudentsByCourse(courseId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/courses/{courseId}/students/search")
    @PreAuthorize("hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getStudentsByCourseAndName(
            @PathVariable Long courseId,
            @RequestParam String studentName) {
        List<User> students = teacherService.getStudentsByCourseAndName(courseId, studentName);
        return ResponseEntity.ok(students);
    }
}
