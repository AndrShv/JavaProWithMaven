package com.example.repository.studying;

import com.example.model.Course;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
    List<Course> findByTeacherId(Long teacherId);
    List<User> findStudentsByCourseIdAndStudentNameContains(Long courseId, String studentName);
}
