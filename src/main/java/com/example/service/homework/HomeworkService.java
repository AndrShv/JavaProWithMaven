package com.example.service.homework;

import com.example.courseDetails.HomeworkStatus;
import com.example.model.Studing.Course;
import com.example.model.Homework.Homework;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final CourseRepository courseRepository;

    public Homework createHomework(Long courseId, String description, LocalDateTime deadline, boolean autoCheck, int maxScore) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Homework homework = new Homework();
        homework.setCourse(course);
        homework.setDescription(description);
        homework.setDeadline(deadline);
        homework.setAutoCheck(autoCheck);
        homework.setMaxScore(maxScore);
        homework.setStatus(HomeworkStatus.ACTIVE);

        return homeworkRepository.save(homework);
    }

    public List<Homework> getAllHomeworksFromCourse(Long courseId) {
        return homeworkRepository.findByCourseId(courseId);
    }
}
