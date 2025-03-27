package com.example.repository.studying;

import com.example.model.HomeworkGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkGradeRepository extends JpaRepository<HomeworkGrade, Long> {
    List<HomeworkGrade> findByHomeworkId(Long homeworkId);
    List<HomeworkGrade> findByHomeworkIdAndTeacherId(Long homeworkId, Long teacherId);
    List<HomeworkGrade> findByCourseIdAndStudentId(Long courseId, Long id);
    List<HomeworkGrade> findByHomeworkIdAndTeacherIdAndStudentId(Long homeworkId, Long teacherId, Long studentId);
}

