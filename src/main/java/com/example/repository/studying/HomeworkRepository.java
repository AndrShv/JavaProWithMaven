package com.example.repository.studying;

import com.example.extraConfigs.HomeworkStatus;
import com.example.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkRepository extends JpaRepository<Homework, Long>{
    List<Homework> findByUserIdAndStatus(Long userId, HomeworkStatus status);
}
