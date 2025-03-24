package com.example.controller.course;

import com.example.dto.request.CourseRequest;
import com.example.dto.response.CourseResponse;
import com.example.model.Achievement;
import com.example.model.Course;
import com.example.model.Lesson;
import com.example.model.User;
import com.example.repository.studying.CourseRepository;
import com.example.repository.users.UserRepository;
import com.example.service.studying.CourseService;
import com.example.service.studying.LessonService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Slf4j
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserRepository userRepository;

    @Autowired
    public CourseController(CourseService courseService, LessonService lessonService, UserRepository userRepository) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userRepository = userRepository;
    }

    @PostMapping("/create-course")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<CourseResponse> createCourse(
            @RequestBody @Valid CourseRequest request,
            Principal principal) {
        log.info("Creating course by teacher: {}", principal.getName());
        String teacherUsername = principal.getName();
        CourseResponse response = courseService.createCourse(request, teacherUsername);
        response.setTitle(request.getTitle());
        response.setDescription(request.getDescription());
        response.setTeacherUsername(teacherUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long courseId,
            @RequestBody @Valid CourseRequest request,
            Principal principal) {
        String teacherUsername = principal.getName();
        CourseResponse response = courseService.updateCourse(courseId, request, teacherUsername);
        response.setTitle(request.getTitle());
        response.setDescription("Updated Description");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{courseId}/addUser/{userId}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> addUserToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        courseService.addUserToCourse(userId, courseId);
        return ResponseEntity.ok("User added to course successfully");
    }

    //test get method
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(authentication);
    }


    @GetMapping("/user/{userId}/achievements")
    @PreAuthorize("hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Set<Achievement>> getUserAchievements(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<Achievement> achievements = user.getAchievements();
            return ResponseEntity.ok(achievements);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId,
            Principal principal) {

        if (principal == null) {
            System.out.println("Principal is null. User is not authenticated.");
            throw new AccessDeniedException("User is not authenticated");
        }

        String teacherUsername = principal.getName();
        System.out.println("Authenticated user: " + teacherUsername);

        courseService.deleteCourse(courseId, teacherUsername);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_STUDENT') or hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT') or hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long courseId) {
        CourseResponse response = courseService.getCourseById(courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/lessons")
    @PreAuthorize("hasAuthority('ROLE_STUDENT') or hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Lesson>> getLessonsByCourse(@PathVariable Long courseId) {
        List<Lesson> lessons = lessonService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(lessons);
    }
}

