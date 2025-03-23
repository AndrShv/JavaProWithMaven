package com.example.service.achievements;

import com.example.extraConfigs.AchievementType;
import com.example.extraConfigs.CourseTheme;
import com.example.model.Achievement;
import com.example.model.Course;
import com.example.model.Homework;
import com.example.model.User;
import com.example.repository.achievement.AchievementRepository;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkRepository;
import com.example.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.extraConfigs.HomeworkStatus.MAX_GRADE;

@Service
public class AchievementCheckService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final HomeworkRepository homeworkRepository;
    private final AchievementCreationService achievementCreationService;
    private final AchievementRepository  achievementRepository;

    public AchievementCheckService(UserRepository userRepository, CourseRepository courseRepository, HomeworkRepository homeworkRepository, AchievementCreationService achievementCreationService, AchievementRepository achievementRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.homeworkRepository = homeworkRepository;
        this.achievementCreationService = achievementCreationService;
        this.achievementRepository = achievementRepository;
    }

    public void checkAchievementOnCompleteCourse(Long userId, Long courseId) {
        achievementAfterEndingFirstCourse(userId, courseId);
        completingTenCourses(userId, courseId);
        finishThreeDifferentCourses(userId, courseId);
        completeCourseWithoutMistakes(userId, courseId);
    }
    public void checkAchievementsOnHomeworkCompletion(Long userId, Long courseId) {
        toDoFirstHomeworkWell(userId, courseId);
        toDoFiveHomeworksInOneDay(userId, courseId);
        tryToDoOneHomeworkFewTimes(userId, courseId);
    }

    public void achievementAfterEndingFirstCourse(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && user.get().getCompletedCourses().size() == 1) {
            Achievement achievement = achievementCreationService.createAchievement(
                    "First Course",
                    AchievementType.COMMON,
                    "Complete your first course");
            achievementRepository.save(achievement);
            achievementCreationService.addAchievementToUser(userId, achievement);
        }
    }

    private void completingTenCourses(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent() && user.get().getCompletedCourses().size() == 10) {
            Achievement achievement = achievementCreationService.createAchievement(
                    "Ten Courses",
                    AchievementType.COMMON,
                    "Complete 10 courses");
            achievementRepository.save(achievement);
            achievementCreationService.addAchievementToUser(userId, achievement);

        }
    }

    public void finishThreeDifferentCourses(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (user.isPresent() && course.isPresent()) {
            Set<CourseTheme> uniqueThemes = user.get().getCompletedCourses().stream()
                    .map(Course::getTheme)
                    .collect(Collectors.toSet());
            if (user.get().getCompletedCourses().size() == 3) {
                Achievement achievement = achievementCreationService.createAchievement(
                        "Polyglot",
                        AchievementType.RARE,
                        "Complete 3 different courses");
                achievementRepository.save(achievement);
                achievementCreationService.addAchievementToUser(userId, achievement);
            }
        }
    }

    public void completeCourseWithoutMistakes(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Homework> homework = homeworkRepository.findByUserAndCourse(user.get(), course.get());

        if (user.isPresent() && course.isPresent()) {
            if (homework.isPresent() && homework.get().getMistakes() == 0 && homework.get().getCountingTries() == 1 && homework.get().getStatus() == MAX_GRADE) {
                Achievement achievement = achievementCreationService.createAchievement(
                        "Student of the Year",
                        AchievementType.LEGENDARY,
                        "Complete a course without making a single mistake on the tests");
                achievementRepository.save(achievement);
                achievementCreationService.addAchievementToUser(userId, achievement);
            }
        }
    }

    public void toDoFirstHomeworkWell(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Homework> homework = homeworkRepository.findByUserAndCourse(user.get(), course.get());

        if (user.isPresent() && course.isPresent() && homework.isPresent()) {
            if (homework.get().getStatus() == MAX_GRADE) {
                Achievement achievement = achievementCreationService.createAchievement(
                        "First Homework On Max Grade Completed",
                        AchievementType.COMMON,
                        "Complete your first homework on max grade");
                achievementRepository.save(achievement);
                achievementCreationService.addAchievementToUser(userId, achievement);

            }
        }
    }

    public void toDoFiveHomeworksInOneDay(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Homework> homework = homeworkRepository.findByUserAndCourse(user.get(), course.get());

        if (user.isPresent() && course.isPresent()) {
            if (homework.isPresent()) {
                Homework homeworkInstance = homework.get();
                if (achievementCreationService.isSubmittedWithin24Hours(homeworkInstance)) {
                    System.out.println("The homework was completed in 24 hours!");
                } else {
                    System.out.println("Homework was submitted later than 24 hours.");
                }
                if (homeworkInstance.getStatus() == MAX_GRADE) {
                    Achievement achievement = achievementCreationService.createAchievement(
                            "Five Homeworks In One Day Completed",
                            AchievementType.COMMON,
                            "Complete 5 homeworks in one day");
                    achievementRepository.save(achievement);
                    achievementCreationService.addAchievementToUser(userId, achievement);

                }
            } else {
                System.out.println("Homework not found for the course.");
            }
        }
    }

    public void tryToDoOneHomeworkFewTimes(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);
        Optional<Homework> homework = homeworkRepository.findByUserAndCourse(user.get(), course.get());

        if (user.isPresent() && homework.isPresent()){
            if(homework.get().getCountingTries() <= 3){
                Achievement achievement = achievementCreationService.createAchievement(
                        "Trying hard - Few Tries",
                        AchievementType.COMMON,
                        "Complete 3 homeworks in one day");
                achievementRepository.save(achievement);
                achievementCreationService.addAchievementToUser(userId, achievement);
            }
        }
    }

    public void getTenDifferentAchievements(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getAchievements().size() == 10 && !achievementRepository.existsByUsersAndName(user, "Ten Achievements Unlocked")) {
                Achievement achievement = achievementCreationService.createAchievement(
                        "Ten Achievements Unlocked",
                        AchievementType.EPIC,
                        "Complete 10 achievements");
                achievementRepository.save(achievement);
                achievementCreationService.addAchievementToUser(userId, achievement);
            }
        }
    }

    public void getTwentyFiveDifferentAchievements(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getAchievements().size() == 25 && !achievementRepository.existsByUsersAndName(user, "Twenty-Five Achievements Unlocked")) {
                Achievement achievement = achievementCreationService.createAchievement(
                        "Twenty-Five Achievements Unlocked",
                        AchievementType.LEGENDARY,
                        "Complete 25 achievements");
                achievementRepository.save(achievement);
                achievementCreationService.addAchievementToUser(userId, achievement);
            }
        }
    }

    public void getAllAchievements(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getAchievements().size() == 50 && !achievementRepository.existsByUsersAndName(user, "All Achievements Unlocked")) {
                Achievement achievement = achievementCreationService.createAchievement(
                        "All Achievements Unlocked",
                        AchievementType.LEGENDARY,
                        "Complete all achievements");
                achievementRepository.save(achievement);
                achievementCreationService.addAchievementToUser(userId, achievement);
            }
        }
    }

    public void checkTeacherFavorite(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            checkTeacherFavorite(user.get().getId());
        }

    }
}
