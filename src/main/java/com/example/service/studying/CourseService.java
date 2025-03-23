package com.example.service.studying;

import com.example.dto.request.CourseRequest;
import com.example.dto.response.CourseResponse;
import com.example.extraConfigs.CourseTheme;
import com.example.extraConfigs.CourseWay;
import com.example.mappers.CourseMapper;
import com.example.model.Course;
import com.example.model.User;
import com.example.rabbitMqConfigs.NotificationService;
import com.example.repository.studying.CourseRepository;
import com.example.repository.users.UserRepository;
import com.example.service.achievements.AchievementCheckService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private AchievementCheckService achievementCheckService;
    @Autowired
    private NotificationService notificationService;

    public CourseResponse createCourse(CourseRequest request, String teacherUsername) {
        System.out.println("Start creating a teacher course: " + teacherUsername);

        User teacher = userRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacher(teacher);
        course.setTeacherEmail(teacher.getEmail());
        course.setStartedTime(LocalDateTime.now());


        if (request.getFinishedTime() != null) {
            try {
                course.setFinishedTime(LocalDateTime.parse(request.getFinishedTime()));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid finishedTime format", e);
            }
        } else {
            throw new IllegalArgumentException("finishedTime cannot be null");
        }


        if (request.getTheme() != null && request.getWay() != null) {
            try {
                course.setTheme(CourseTheme.valueOf(request.getTheme()));
                course.setWay(CourseWay.valueOf(request.getWay()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid theme or way value", e);
            }
        } else {
            throw new IllegalArgumentException("Theme and Way cannot be null");
        }
        Course savedCourse = courseRepository.save(course);
        System.out.println("Course successfully saved: " + savedCourse.getTitle());
        CourseResponse response = courseMapper.toResponse(savedCourse);
        response.setStartedTime(savedCourse.getStartedTime());
        response.setFinishedTime(savedCourse.getFinishedTime());
        response.setTheme(savedCourse.getTheme().toString());
        response.setWay(savedCourse.getWay().toString());
        String notificationText = "A new course has been created: " + savedCourse.getTitle();
        notificationService.sendAsyncNotification(teacher.getEmail(), "New Course", notificationText);
        System.out.println("Notification sent");

        return response;
    }


    public CourseResponse updateCourse(Long courseId, CourseRequest request, String teacherUsername) {
        System.out.println("Starting course update with ID: " + courseId + " for teacher: " + teacherUsername);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (!course.getTeacher().getUsername().equals(teacherUsername)) {
            System.out.println("Teacher is not authorized to update the course");
            throw new AccessDeniedException("You are not allowed to update this course");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        System.out.println("Course successfully updated: " + course.getTitle());

        return courseMapper.toResponse(course);
    }

    public void deleteCourse(Long courseId, String teacherUsername) {
        System.out.println("Starting course deletion with ID: " + courseId + " for teacher: " + teacherUsername);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (!course.getTeacher().getUsername().equals(teacherUsername)) {
            System.out.println("Teacher is not authorized to delete the course");
            throw new AccessDeniedException("You are not allowed to delete this course");
        }

        courseRepository.delete(course);
        System.out.println("Course successfully deleted: " + course.getTitle());
    }

    public List<CourseResponse> getAllCourses() {
        System.out.println("Retrieving all courses");
        return courseMapper.toResponseList(courseRepository.findAll());
    }

    public CourseResponse getCourseById(Long courseId) {
        System.out.println("Retrieving course with ID: " + courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        System.out.println("Course found: " + course.getTitle());
        return courseMapper.toResponse(course);
    }

    @Transactional
    public void addUserToCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        if (course.getStudents().contains(user)) {
            throw new IllegalStateException("User is already enrolled in this course");
        }

        course.getStudents().add(user);
        courseRepository.save(course);
    }

    public void completeCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        user.getCompletedCourses().add(course);
        userRepository.save(user);
        achievementCheckService.achievementAfterEndingFirstCourse(userId, course.getId());
        achievementCheckService.finishThreeDifferentCourses(userId, course.getId());
        achievementCheckService.completeCourseWithoutMistakes(userId, course.getId());
        achievementCheckService.getTenDifferentAchievements(userId);
        achievementCheckService.getTwentyFiveDifferentAchievements(userId);
        achievementCheckService.getAllAchievements(userId);
        achievementCheckService.checkTeacherFavorite(user.getId());

        System.out.println("User " + user.getUsername() + " has completed course " + course.getTitle());
    }
}
