package com.example.service;
import com.example.dto.CourseRequest;
import com.example.dto.CourseResponse;
import com.example.mappers.CourseMapper;
import com.example.model.Course;
import com.example.model.User;
import com.example.repository.CourseRepository;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseMapper = courseMapper;
    }

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
