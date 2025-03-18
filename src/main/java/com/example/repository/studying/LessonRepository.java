package com.example.repository.studying;

import com.example.model.Course;
import com.example.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>{
    List<Lesson> findByCourse(Course course);
}
