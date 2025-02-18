package com.example.repository.studying;

import com.example.courseDetails.SubmissionStatus;
import com.example.model.Homework.HomeworkSubmission;
import com.example.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkSubmissionRepository extends JpaRepository<HomeworkSubmission, Long>{
    List<User>findAllHomeworkByStudentId(Long studentId);
    List<SubmissionStatus> findByStudentId(Long studentId);
    List<SubmissionStatus> findByHomeworkId(Long homeworkId);
}
