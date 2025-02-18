package com.example.repository.studying;

import com.example.model.Homework.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkRepository extends JpaRepository<Homework, Long>{
    List<Homework> findByCourseId(Long courseId);

}
