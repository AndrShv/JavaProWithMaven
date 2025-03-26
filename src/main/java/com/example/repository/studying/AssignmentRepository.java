package com.example.repository.studying;

import com.example.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long>{
    List<Assignment> findByCourseIdAndUserId(Long courseId, Long userId);
}
