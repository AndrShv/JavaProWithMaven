package com.example.controller.course;

import com.example.model.Homework.HomeworkSubmission;
import com.example.service.homework.HomeworkSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/homework-submissions")
@RequiredArgsConstructor
public class HomeworkSubmissionController {
    private final HomeworkSubmissionService submissionService;

    @PostMapping("/submit/{homeworkId}")
    public ResponseEntity<HomeworkSubmission> submitHomework(@PathVariable Long homeworkId, @RequestBody HomeworkSubmission submission) {
        HomeworkSubmission savedSubmission = submissionService.submitHomework(homeworkId, submission.getStudent().getId(), submission.getAnswer(), submission.getFilePath());
        return ResponseEntity.ok(savedSubmission);
    }
}