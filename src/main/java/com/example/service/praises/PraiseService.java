package com.example.service.praises;

import com.example.model.Praise;
import com.example.model.Teacher;
import com.example.model.User;
import com.example.repository.praises.PraiseRepository;
import com.example.repository.users.TeacherRepository;
import com.example.repository.users.UserRepository;
import com.example.service.achievements.AchievementService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PraiseService {
    private final PraiseRepository praiseRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final AchievementService achievementService;

    public PraiseService(PraiseRepository praiseRepository, UserRepository userRepository, TeacherRepository teacherRepository, AchievementService achievementService) {
        this.praiseRepository = praiseRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.achievementService = achievementService;
    }

    public void givePraise(Long teacherId, Long studentId, String message) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        Optional<User> student = userRepository.findById(studentId);

        if (teacher.isPresent() && student.isPresent()) {
            Praise praise = new Praise();
            praise.setTeacher(teacher.get());
            praise.setStudent(student.get());
            praise.setMessage(message);
            praiseRepository.save(praise);

            achievementService.checkTeacherFavorite(student.get().getId());
        }
    }
}
