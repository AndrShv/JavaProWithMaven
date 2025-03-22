package com.example.service.studying;

import com.example.dto.request.HomeworkRequest;
import com.example.extraConfigs.HomeworkStatus;
import com.example.model.Course;
import com.example.model.Homework;
import com.example.model.Lesson;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkRepository;
import com.example.repository.studying.LessonRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LessonService {

    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final CourseRepository courseRepository;

    public LessonService(LessonRepository lessonRepository, HomeworkRepository homeworkRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.homeworkRepository = homeworkRepository;
        this.courseRepository = courseRepository;
    }

    // Создание урока
    public Lesson createLesson(Long courseId, Lesson lesson) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        lesson.setCourse(course); // Привязка к найденному курсу
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
        System.out.println("Lesson found: " + lesson.getTitle());
        Hibernate.initialize(lesson.getHomeworks());
        return lesson.getHomeworks();
    }




    // Оценка домашнего задания
    public Homework gradeHomework(Long homeworkId, int grade) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RuntimeException("Homework not found"));
        homework.setGrade(grade);
        homework.setStatus(grade == 100 ? HomeworkStatus.DONE : HomeworkStatus.IN_PROGRESS); // Изменение статуса
        return homeworkRepository.save(homework);
    }

    // Добавление комментария к домашнему заданию
    public Homework commentHomework(Long homeworkId, String comment) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new RuntimeException("Homework not found"));
        homework.setComment(comment); // Установка комментария
        return homeworkRepository.save(homework);
    }

    // Получение урока
    public Lesson getLesson(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    public List<Lesson> getLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return lessonRepository.findByCourse(course);
    }


}
