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
import com.example.service.achievements.AchievementService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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
    private AchievementService achievementService;

    @Autowired
    private NotificationService notificationService;

    public CourseResponse createCourse(CourseRequest request, String teacherUsername) {
        System.out.println("Start creating a teacher course: " + teacherUsername);
        System.out.println("Searching for teacher: " + teacherUsername);
        User teacher = userRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        System.out.println("Teacher found: " + teacher.getUsername() + " (ID: " + teacher.getId() + ")");
        System.out.println("Teacher found: " + teacher.getUsername());

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

        String notificationText = "A new course has been created: " + savedCourse.getTitle();
        notificationService.sendAsyncNotification(teacher.getEmail(), "New Course", notificationText);
        System.out.println("Notification sent");


        return courseMapper.toResponse(savedCourse);
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

    public void addUserToCourse(Long userId, Long courseId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (userOptional.isPresent() && courseOptional.isPresent()) {
            User user = userOptional.get();
            Course course = courseOptional.get();
            course.getStudents().add(user);
            user.getCourses().add(course);
            courseRepository.save(course);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User or Course not found");
        }
    }

    public void completeCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));


        user.getCompletedCourses().add(course);
        userRepository.save(user);
        achievementService.addAchievementAfterEndingFirstCourse(userId, course);
        achievementService.completingTenCourse(userId, course);
        achievementService.completingTwentyFiveCourse(userId, course);
        achievementService.finishThreeDifferentCourse(userId, course);
        achievementService.completeCourseWithoutMistakes(userId, course);
        achievementService.getTenDifferentAchievements(userId, course);
        achievementService.getTwentyFiveDifferentAchievements(userId, course);
        achievementService.getAllAchievements(userId, course);
        achievementService.checkTeacherFavorite(user.getId());

        System.out.println("User " + user.getUsername() + " has completed course " + course.getTitle());
    }
}
