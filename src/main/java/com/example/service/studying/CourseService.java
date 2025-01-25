package com.example.service.studying;

import com.example.dto.request.CourseRequest;
import com.example.dto.response.CourseResponse;
import com.example.mappers.CourseMapper;
import com.example.model.Course;
import com.example.model.User;
import com.example.rabbitMqConfigs.NotificationService;
import com.example.repository.studying.CourseRepository;
import com.example.repository.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private NotificationService notificationService;

    public CourseResponse createCourse(CourseRequest request, String teacherUsername) {
        System.out.println("Начало создания курса для преподавателя: " + teacherUsername);
        User teacher = userRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        System.out.println("Преподаватель найден: " + teacher.getUsername());

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacher(teacher);

        Course savedCourse = courseRepository.save(course);
        System.out.println("Курс успешно сохранен: " + savedCourse.getTitle());

        String notificationText = "Новый курс был создан: " + savedCourse.getTitle();
        notificationService.sendAsyncNotification(teacher.getEmail(), "Новый курс", notificationText);
        System.out.println("Уведомление отправлено");

        return courseMapper.toResponse(savedCourse);
    }

    public CourseResponse updateCourse(Long courseId, CourseRequest request, String teacherUsername) {
        System.out.println("Начало обновления курса с ID: " + courseId + " для преподавателя: " + teacherUsername);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (!course.getTeacher().getUsername().equals(teacherUsername)) {
            System.out.println("Преподаватель не авторизован для обновления курса");
            throw new AccessDeniedException("You are not allowed to update this course");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        System.out.println("Курс успешно обновлен: " + course.getTitle());

        return courseMapper.toResponse(course);
    }

    public void deleteCourse(Long courseId, String teacherUsername) {
        System.out.println("Начало удаления курса с ID: " + courseId + " для преподавателя: " + teacherUsername);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (!course.getTeacher().getUsername().equals(teacherUsername)) {
            System.out.println("Преподаватель не авторизован для удаления курса");
            throw new AccessDeniedException("You are not allowed to delete this course");
        }

        courseRepository.delete(course);
        System.out.println("Курс успешно удален: " + course.getTitle());
    }

    public List<CourseResponse> getAllCourses() {
        System.out.println("Получение всех курсов");
        return courseMapper.toResponseList(courseRepository.findAll());
    }

    public CourseResponse getCourseById(Long courseId) {
        System.out.println("Получение курса с ID: " + courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        System.out.println("Курс найден: " + course.getTitle());
        return courseMapper.toResponse(course);
    }
}
