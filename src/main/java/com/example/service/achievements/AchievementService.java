package com.example.service.achievements;

import com.example.extraConfigs.CourseTheme;
import com.example.extraConfigs.HomeworkStatus;
import com.example.model.*;
import com.example.repository.achievement.AchievementRepository;
import com.example.repository.praises.PraiseRepository;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkRepository;
import com.example.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.extraConfigs.AchievementType.*;
import static com.example.extraConfigs.HomeworkStatus.MAX_GRADE;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final PraiseRepository praiseRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final HomeworkRepository homeworkRepository;

    public AchievementService(AchievementRepository achievementRepository, PraiseRepository praiseRepository, UserRepository userRepository, CourseRepository courseRepository, HomeworkRepository homeworkRepository) {
        this.achievementRepository = achievementRepository;
        this.praiseRepository = praiseRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.homeworkRepository = homeworkRepository;
    }

    // Автоматическая проверка достижений при завершении курса
    public void checkAchievementsOnCourseCompletion(Long userId, Course courseId) {
        addAchievementAfterEndingFirstCourse(userId, courseId);
        completingTenCourse(userId, courseId);
        completingTwentyFiveCourse(userId, courseId);
        finishThreeDifferentCourse(userId, courseId);
        completeCourseWithoutMistakes(userId, courseId);
        completeCourseWithoutMistakesMaster(userId, courseId);
    }

    // Проверка достижений при выполнении домашки
    public void checkAchievementsOnHomeworkCompletion(Long userId, Course courseId) {
        toDoFirstHomeworkWell(userId, courseId);
        toDoFiveHomeworksInOneDay(userId, courseId);
        tryToDoOneHomeworkFewTimes(userId, courseId);
    }

    // Проверка достижений при получении разных наград
    public void checkAchievementsOnNewAchievements(Long userId, Course courseId) {
        getTenDifferentAchievements(userId, courseId);
        getTwentyFiveDifferentAchievements(userId, courseId);
        getAllAchievements(userId, courseId);
    }

    // Проверка достижений учителя
    public void checkTeacherFavorite(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            checkTeacherFavorite(user.get().getId());
        }
    }

    // Основные методы для выдачи достижений:
    public void addAchievementAfterEndingFirstCourse(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (user.get().getCompletedCourses().size() == 1) {
                Achievement achievement = new Achievement();
                achievement.setName("First Course Completed");
                achievement.setRarity(COMMON);
                achievement.setDescription("Congratulations! You've completed your first course. Keep up the great work!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    public void completingTenCourse(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (user.get().getCompletedCourses().size() == 10) {
                Achievement achievement = new Achievement();
                achievement.setName("Ten Courses Completed");
                achievement.setRarity(LEGENDARY);
                achievement.setDescription("Congratulations! You've completed 10 courses!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    public void completingTwentyFiveCourse(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (user.get().getCompletedCourses().size() == 25) {
                Achievement achievement = new Achievement();
                achievement.setName("Twenty Five Courses Completed");
                achievement.setRarity(EPIC);
                achievement.setDescription("Congratulations! You've completed 25 courses!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    public void finishThreeDifferentCourse(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            Set<CourseTheme> uniqueThemes = user.get().getCompletedCourses().stream()
                    .map(Course::getTheme)
                    .collect(Collectors.toSet());
            if (user.get().getCompletedCourses().size() >= 3) {
                Achievement achievement = new Achievement();
                achievement.setName("Polyglot");
                achievement.setRarity(RARE);
                achievement.setDescription("Polyglot - you know many themes!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    public void completeCourseWithoutMistakes(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (user.get().getCompletedCourses().size() == 1) {
                Achievement achievement = new Achievement();
                achievement.setName("Student of the Year");
                achievement.setRarity(LEGENDARY);
                achievement.setDescription("Student of the Year - you completed the course without making a single mistake on the tests!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    public void completeCourseWithoutMistakesMaster(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());
        Optional<Homework> homework = homeworkRepository.findByUserAndCourse(user.get(), course.get());

        if (user.isPresent() && course.isPresent() && homework.isPresent()) {
            if (course.get().isPassed() && homework.get().getMistakes() == 0 && homework.get().getCountingTries() == 1 && homework.get().getStatus() == MAX_GRADE) {
                Achievement achievement = new Achievement();
                achievement.setName("Master of the Course");
                achievement.setRarity(LEGENDARY);
                achievement.setDescription("Master of the Course - you completed the course without making a single mistake on the tests!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    // Дополнительные методы для достижений
    public void toDoFirstHomeworkWell(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());
        Optional<Homework> homework = homeworkRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent() && homework.isPresent()) {
            if (homework.get().getStatus() == MAX_GRADE) {
                Achievement achievement = new Achievement();
                achievement.setName("First Homework On Max Grade Completed");
                achievement.setRarity(COMMON);
                achievement.setDescription("Congratulations! You've completed your first homework on max grade.");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    public boolean isSubmittedWithin24Hours(Homework homework) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime doneAt = homework.getDoneAtTime();
        Duration duration = Duration.between(doneAt, now);
        return duration.toHours() <= 24;
    }

    public void toDoFiveHomeworksInOneDay(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());
        Optional<Homework> homework = homeworkRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (homework.isPresent()) {
                Homework homeworkInstance = homework.get();
                if (isSubmittedWithin24Hours(homeworkInstance)) {
                    System.out.println("The homework was completed in 24 hours!");
                } else {
                    System.out.println("Homework was submitted later than 24 hours.");
                }
                if (homeworkInstance.getStatus() == MAX_GRADE) {
                    Achievement achievement = new Achievement();
                    achievement.setName("Five Homeworks In One Day Completed");
                    achievement.setRarity(COMMON);
                    achievement.setDescription("Congratulations! You've completed 5 homeworks in one day.");
                    user.get().getAchievements().add(achievement);
                    userRepository.save(user.get());
                }
            } else {
                System.out.println("Homework not found for the course.");
            }
        } else {
            System.out.println("User or Course not found.");
        }
    }

    public void tryToDoOneHomeworkFewTimes(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());
        Optional<Homework> homework = homeworkRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (homework.isPresent() && homework.get().getCountingTries() <= 3) {
                Achievement achievement = new Achievement();
                achievement.setName("Trying hard - Few Tries");
                achievement.setRarity(COMMON);
                achievement.setDescription("Well done! You made multiple attempts to do the homework.");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    public void getTenDifferentAchievements(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Set<Achievement> achievements = user.get().getAchievements();
            if (achievements.size() >= 10) {
                Achievement achievement = new Achievement();
                achievement.setName("Ten Achievements Unlocked");
                achievement.setRarity(EPIC);
                achievement.setDescription("Congratulations! You've unlocked 10 different achievements!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    public void getTwentyFiveDifferentAchievements(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Set<Achievement> achievements = user.get().getAchievements();
            if (achievements.size() >= 25) {
                Achievement achievement = new Achievement();
                achievement.setName("Twenty Five Achievements Unlocked");
                achievement.setRarity(LEGENDARY);
                achievement.setDescription("Amazing! You've unlocked 25 different achievements!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    public void getAllAchievements(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Set<Achievement> achievements = user.get().getAchievements();
            Achievement achievement = new Achievement();
            achievement.setName("All Achievements Unlocked");
            achievement.setRarity(ULTIMATE);
            achievement.setDescription("Incredible! You've unlocked all the achievements available!");
            user.get().getAchievements().add(achievement);
            userRepository.save(user.get());
        }
    }

}
