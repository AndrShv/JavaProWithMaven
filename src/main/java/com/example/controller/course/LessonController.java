package com.example.controller.course;

import com.example.dto.request.HomeworkGradeRequest;
import com.example.dto.request.HomeworkRequest;
import com.example.dto.response.HomeworkGradeResponse;
import com.example.dto.response.HomeworkResponse;
import com.example.dto.response.LessonResponse;
import com.example.model.Homework;
import com.example.model.HomeworkGrade;
import com.example.model.Lesson;
import com.example.repository.studying.HomeworkGradeRepository;
import com.example.service.studying.LessonService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@PreAuthorize("hasAuthority('ROLE_TEACHER')")
public class LessonController {

    private final LessonService lessonService;
    private final HomeworkGradeRepository homeworkGradeRepository;

    public LessonController(LessonService lessonService, HomeworkGradeRepository homeworkGradeRepository) {
        this.lessonService = lessonService;
        this.homeworkGradeRepository = homeworkGradeRepository;
    }

    // Создание урока
    @PostMapping(value = "/{courseId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public Lesson createLesson(@RequestBody Lesson lesson, @PathVariable Long courseId) {
        return lessonService.createLesson(courseId, lesson);
    }

    // Получение урока
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public Lesson getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id);
    }

    // Создание домашнего задания для урока
    @PostMapping("/{lessonId}/homework")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public HomeworkResponse createHomework(@PathVariable Long lessonId, @RequestBody HomeworkRequest homeworkRequest) {
        Homework homework = lessonService.createHomework(lessonId, homeworkRequest);

        HomeworkResponse response = new HomeworkResponse();
        response.setId(homework.getId());
        response.setTitle(homework.getTitle());
        response.setDescription(homework.getDescription());
        response.setDoneAtTime(homework.getDoneAtTime());
        response.setLessonId(homework.getLesson().getId());
        response.setGrade(homework.getGrade());
        response.setComment(homework.getComment());

        return response;
    }
    //обновить дз
    @PutMapping("/{lessonId}/homework/{homeworkId}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<HomeworkResponse> updateHomework(
            @PathVariable Long lessonId,
            @PathVariable Long homeworkId,
            @RequestBody HomeworkRequest request) {

        HomeworkResponse response = lessonService.updateHomework(homeworkId, request);
        return ResponseEntity.ok(response);
    }
    //обновить урок
    @PutMapping("/{lessonId}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public LessonResponse updateLesson(@PathVariable Long lessonId, @RequestBody Lesson lesson) {
        return lessonService.updateLesson(lessonId, lesson);
    }

    // Оценка домашнего задания
    @PutMapping("/{lessonId}/homework/{homeworkId}/grade")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<HomeworkGradeResponse> gradeHomework(
            @PathVariable Long lessonId,
            @PathVariable Long homeworkId,
            @RequestBody HomeworkGradeRequest request) {

        HomeworkGradeResponse response = lessonService.gradeHomework(request);
        return ResponseEntity.ok(response);
    }


    // Добавление комментария к домашнему заданию
    @PutMapping("/{lessonId}/homework/{homeworkId}/comment")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public Homework commentHomework(@PathVariable Long homeworkId, @RequestParam String comment) {
        return lessonService.commentHomework(homeworkId, comment);
    }

    @GetMapping("/{lessonId}/homeworks")
    @PreAuthorize("hasAuthority('ROLE_STUDENT') or hasAuthority('ROLE_TEACHER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Homework>> getHomeworksByLesson(@PathVariable Long lessonId) {
        List<Homework> homeworks = lessonService.getHomeworksByLessonId(lessonId);
        return ResponseEntity.ok(homeworks);
    }

    @GetMapping("/{lessonId}/homework/{homeworkId}/grades")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<List<HomeworkGrade>> getHomeworkGrades(@PathVariable Long homeworkId) {
        List<HomeworkGrade> grades = lessonService.getHomeworkGrades(homeworkId);
        return ResponseEntity.ok(grades);
    }
    @DeleteMapping("/{lessonId}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public void deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
    }
    @DeleteMapping("/{lessonId}/homework/{homeworkId}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public void deleteHomework(@PathVariable Long homeworkId) {
        lessonService.deleteHomework(homeworkId);
    }
}

