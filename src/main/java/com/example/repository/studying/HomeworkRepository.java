package com.example.repository.studying;

import com.example.extraConfigs.HomeworkStatus;
import com.example.model.Course;
import com.example.model.Homework;
import com.example.model.Lesson;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long>{
    List<Homework> findByUserIdAndStatus(Long userId, HomeworkStatus status);
    Optional<Homework> findByUserAndCourse(User user, Course course);
    List<Homework> findByLesson(Lesson lesson);
}
