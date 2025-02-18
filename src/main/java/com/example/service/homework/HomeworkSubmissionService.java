package com.example.service.homework;


import com.example.model.Homework.HomeworkSubmission;

import com.example.repository.studying.HomeworkRepository;
import com.example.repository.studying.HomeworkSubmissionRepository;
import com.example.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeworkSubmissionService {
    private final HomeworkSubmissionRepository submissionRepository;
    private final HomeworkRepository homeworkRepository;
    private final UserRepository studentRepository;

    public HomeworkSubmission submitHomework(Long homeworkId, Long studentId, String answer, String filePath) {

        var homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RuntimeException("Homework not found"));
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        HomeworkSubmission submission = new HomeworkSubmission();
        submission.setHomework(homework);
        submission.setStudent(student);
        submission.setAnswer(answer);
        submission.setFilePath(filePath);
        return submissionRepository.save(submission);
    }
}
