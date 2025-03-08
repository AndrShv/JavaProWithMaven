package com.example.service.studying;

import com.example.extraConfigs.CourseWay;
import com.example.extraConfigs.HomeworkStatus;
import com.example.model.Course;
import com.example.model.Homework;
import com.example.model.Lesson;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkRepository;
import com.example.repository.studying.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // Создание домашнего задания для урока
    // Создание домашнего задания
    public Homework createHomework(Long lessonId, Homework homework) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        Course course = courseRepository.findById(lesson.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        System.out.println("Found lesson: " + lesson.getTitle());
        homework.setLesson(lesson);
        homework.setStatus(HomeworkStatus.IN_PROGRESS);
        homework.setDoneAtTime(LocalDateTime.now());

        Homework savedHomework = homeworkRepository.save(homework);

        lesson.getHomeworks().add(savedHomework);
        lessonRepository.save(lesson);

        System.out.println("Saved Homework: " + savedHomework);
        System.out.println("Homework lesson: " + savedHomework.getLesson());

        return savedHomework;
    }

    public List<Homework> getHomeworksForLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        System.out.println("Found lesson: " + lesson.getTitle());
        return homeworkRepository.findByLesson(lesson);
    }
    public List<Homework> getHomeworksByLessonId(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        System.out.println("Lesson found: " + lesson.getTitle());
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
