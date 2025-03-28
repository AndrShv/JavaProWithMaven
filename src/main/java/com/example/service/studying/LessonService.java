package com.example.service.studying;

import com.example.dto.request.HomeworkGradeRequest;
import com.example.dto.request.HomeworkRequest;
import com.example.dto.response.HomeworkGradeResponse;
import com.example.dto.response.HomeworkResponse;
import com.example.dto.response.LessonResponse;
import com.example.extraConfigs.HomeworkStatus;
import com.example.mappers.HomeworkGradeMapper;
import com.example.model.*;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkGradeRepository;
import com.example.repository.studying.HomeworkRepository;
import com.example.repository.studying.LessonRepository;
import com.example.repository.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mysql.cj.conf.PropertyKey.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Transactional
public class LessonService {
    private final HomeworkGradeRepository homeworkGradeRepository;
    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final HomeworkGradeMapper homeworkGradeMapper;
    private static final Logger logger = LoggerFactory.getLogger(LessonService.class);


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

    @Transactional
    public Homework createHomework(Long lessonId, HomeworkRequest homeworkRequest) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + lessonId));

        if (homeworkRequest.getTitle() == null || homeworkRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Homework title cannot be null or empty");
        }
        if (homeworkRequest.getDescription() == null || homeworkRequest.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Homework description cannot be null or empty");
        }

        Homework homework = new Homework();
        homework.setTitle(homeworkRequest.getTitle());
        homework.setDescription(homeworkRequest.getDescription());

        if (homeworkRequest.getStatus() != null && !homeworkRequest.getStatus().trim().isEmpty()) {
            try {
                homework.setStatus(HomeworkStatus.valueOf(homeworkRequest.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value: " + homeworkRequest.getStatus());
            }
        } else {
            homework.setStatus(HomeworkStatus.IN_PROGRESS);
        }

        homework.setCountingTries(0);
        homework.setMistakes(0);
        homework.setGrade(0);

        homework.setLesson(lesson);

        if (homeworkRequest.getUserId() != null) {
            User user = userRepository.findById(homeworkRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + homeworkRequest.getUserId()));
            homework.setUser(user);
            logger.info("User assigned to homework: {}", user.getUsername());
        } else {
            logger.warn("User ID is null in the homework request.");
        }

        if (homeworkRequest.getDoneAtTime() != null && !homeworkRequest.getDoneAtTime().trim().isEmpty()) {
            try {
                homework.setDoneAtTime(LocalDateTime.parse(homeworkRequest.getDoneAtTime()));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid doneAtTime format. Expected format: yyyy-MM-dd'T'HH:mm:ss");
            }
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
        if (existingHomeworkGrades.size() > 1) {
            throw new RuntimeException("Multiple grades found for the same homework, teacher, and student");
        }

        HomeworkGrade homeworkGrade = existingHomeworkGrades.isEmpty() ? new HomeworkGrade() : existingHomeworkGrades.get(0);
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

    @Transactional
    public LessonResponse updateLesson(Long lessonId, Lesson lessonRequest) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        if (lessonRequest.getTitle() != null) {
            lesson.setTitle(lessonRequest.getTitle());
        }
        if (lessonRequest.getContent() != null) {
            lesson.setContent(lessonRequest.getContent());
        }

        // Обновляем курс, если передан ID
        if (lessonRequest.getCourse() != null && lessonRequest.getCourse().getId() != null) {
            Course course = courseRepository.findById(lessonRequest.getCourse().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));
            lesson.setCourse(course);
        }

        // Обновление домашек
        if (lessonRequest.getHomeworks() != null) {
            for (Homework homework : lessonRequest.getHomeworks()) {
                if (homework.getId() == null) {
                    // Новая домашка
                    homework.setLesson(lesson);
                } else {
                    // Обновление существующей
                    Homework existingHomework = homeworkRepository.findById(homework.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Homework not found"));
                    existingHomework.setTitle(homework.getTitle());
                    existingHomework.setDescription(homework.getDescription());
                }
            }
        }

        Lesson updatedLesson = lessonRepository.save(lesson);

        return new LessonResponse(
                updatedLesson.getId(),
                updatedLesson.getTitle(),
                updatedLesson.getContent(),
                updatedLesson.getCourse().getId()
        );
    }


    public HomeworkResponse updateHomework(Long homeworkId, HomeworkRequest request) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RuntimeException("Homework not found"));

        if (request.getTitle() != null) {
            homework.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            homework.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            homework.setStatus(HomeworkStatus.valueOf(request.getStatus()));
        }
        if (request.getDoneAtTime() != null) {
            homework.setDoneAtTime(LocalDateTime.parse(request.getDoneAtTime()));
        }
        HomeworkResponse response = new HomeworkResponse();
        response.setId(homework.getId());
        response.setTitle(homework.getTitle());
        response.setDescription(homework.getDescription());
        response.setDoneAtTime(homework.getDoneAtTime());
        response.setLessonId(homework.getLesson().getId());
        response.setGrade(homework.getGrade());
        response.setComment(homework.getComment());
        Homework updatedHomework = homeworkRepository.save(homework);
        return response;
    }

    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

    @Transactional
    public void deleteHomework(Long homeworkId) {
        homeworkGradeRepository.deleteByHomeworkId(homeworkId);
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RuntimeException("Homework not found"));
        homeworkRepository.delete(homework);
    }
}
