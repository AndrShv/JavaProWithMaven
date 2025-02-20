package com.example.service.teacher;


import com.example.model.User;
import com.example.model.Course;
import com.example.repository.studying.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final CourseRepository courseRepository;


    public List<User> getStudentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return course.getStudents();
    }
    public List<User> getStudentsByCourseAndName(Long courseId, String studentName) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return course.getStudents().stream()
                .filter(student -> student.getUsername().equalsIgnoreCase(studentName))
                .collect(Collectors.toList());
    }


}

