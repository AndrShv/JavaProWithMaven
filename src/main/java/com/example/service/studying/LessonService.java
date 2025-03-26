package com.example.service.studying;

import com.example.dto.request.HomeworkGradeRequest;
import com.example.dto.request.HomeworkRequest;
import com.example.dto.response.HomeworkGradeResponse;
import com.example.extraConfigs.HomeworkStatus;
import com.example.mappers.HomeworkGradeMapper;
import com.example.model.*;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkGradeRepository;
import com.example.repository.studying.HomeworkRepository;
import com.example.repository.studying.LessonRepository;
import com.example.repository.users.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonService {
    private final HomeworkGradeRepository homeworkGradeRepository;
    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final HomeworkGradeMapper homeworkGradeMapper;

    public LessonService(HomeworkGradeRepository homeworkGradeRepository, LessonRepository lessonRepository, HomeworkRepository homeworkRepository, CourseRepository courseRepository, UserRepository userRepository, HomeworkGradeMapper homeworkGradeMapper) {
        this.homeworkGradeRepository = homeworkGradeRepository;
        this.lessonRepository = lessonRepository;
        this.homeworkRepository = homeworkRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.homeworkGradeMapper = homeworkGradeMapper;
    }

    public Lesson createLesson(Long courseId, Lesson lesson) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    public Homework createHomework(Long lessonId, HomeworkRequest homeworkRequest) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        if (homeworkRequest.getTitle() == null || homeworkRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Homework title cannot be null or empty");
        }
        if (homeworkRequest.getDescription() == null || homeworkRequest.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Homework description cannot be null or empty");
        }
        Homework homework = new Homework();
        homework.setTitle(homeworkRequest.getTitle());
        homework.setDescription(homeworkRequest.getDescription());
        homework.setStatus(HomeworkStatus.IN_PROGRESS);
        homework.setCountingTries(0);
        homework.setMistakes(0);
        homework.setLesson(lesson);
        homework.setGrade(0);
        if (homeworkRequest.getUserId() != null) {
            User user = userRepository.findById(homeworkRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            homework.setUser(user);
            System.out.println("User set: " + user.getUsername());
        }else {
            System.out.println("User ID is null in request!");

        }

        if (homeworkRequest.getDoneAtTime() != null && !homeworkRequest.getDoneAtTime().isEmpty()) {
            homework.setDoneAtTime(LocalDateTime.parse(homeworkRequest.getDoneAtTime()));
        } else {
            homework.setDoneAtTime(LocalDateTime.now());
        }
        Homework savedHomework = homeworkRepository.save(homework);
        lesson.getHomeworks().add(savedHomework);
        lessonRepository.save(lesson);
        return savedHomework;
    }

    public List<Homework> getHomeworksByLessonId(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        Hibernate.initialize(lesson.getHomeworks());
        return lesson.getHomeworks();
    }

    public HomeworkGradeResponse gradeHomework(HomeworkGradeRequest request) {
        Homework homework = homeworkRepository.findById(request.getHomeworkId())
                .orElseThrow(() -> new RuntimeException("Homework not found"));
        Hibernate.initialize(homework.getLesson());
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!homework.getLesson().getCourse().getStudents().contains(student)) {
            throw new RuntimeException("Student is not enrolled in the course of this homework");
        }
        List<HomeworkGrade> existingHomeworkGrades = homeworkGradeRepository.findByHomeworkIdAndTeacherIdAndStudentId(
                request.getHomeworkId(), request.getTeacherId(), request.getStudentId());
        if (!existingHomeworkGrades.isEmpty()) {
            throw new RuntimeException("Grade for this homework already exists");
        }
        HomeworkGrade homeworkGrade = new HomeworkGrade();
        homeworkGrade.setHomework(homework);
        homeworkGrade.setTeacher(teacher);
        homeworkGrade.setCourse(homework.getLesson().getCourse());
        homeworkGrade.setStudent(student);
        homeworkGrade.setGrade(request.getGrade());
        homework.setGrade(request.getGrade());
        HomeworkGrade savedHomeworkGrade = homeworkGradeRepository.save(homeworkGrade);
        return homeworkGradeMapper.toResponse(savedHomeworkGrade);
    }

    public Homework commentHomework(Long homeworkId, String comment) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RuntimeException("Homework not found"));
        homework.setComment(comment);
        Homework updatedHomework = homeworkRepository.save(homework);
        Hibernate.initialize(updatedHomework.getUser());
        Hibernate.initialize(updatedHomework.getLesson());
        return updatedHomework;
    }

    public Lesson getLesson(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    public List<Lesson> getLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return lessonRepository.findByCourse(course);
    }
    public List<HomeworkGrade> getHomeworkGrades(Long homeworkId) {
        return homeworkGradeRepository.findByHomeworkId(homeworkId);
    }


}
