package com.example.controller.course;

import com.example.dto.request.CourseRequest;
import com.example.dto.response.CourseResponse;
import com.example.service.studying.CourseService;
import jakarta.validation.Valid;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create-course")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponse> createCourse(
            @RequestBody @Valid CourseRequest request,
            Principal principal) {
        String teacherUsername = principal.getName();
        CourseResponse response = courseService.createCourse(request, teacherUsername);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(response);
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long courseId,
            @RequestBody @Valid CourseRequest request,
            Principal principal) {
        String teacherUsername = principal.getName();
        CourseResponse response = courseService.updateCourse(courseId, request, teacherUsername);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId,
            Principal principal) {
        String teacherUsername = principal.getName();
        courseService.deleteCourse(courseId, teacherUsername);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long courseId) {
        CourseResponse response = courseService.getCourseById(courseId);
        return ResponseEntity.ok(response);
    }
}

