package com.example.service.achievements;

import com.example.extraConfigs.HomeworkStatus;
import com.example.model.Achievement;
import com.example.model.Course;
import com.example.model.Homework;
import com.example.model.User;
import com.example.repository.achievement.AchievementRepository;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkRepository;
import com.example.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.extraConfigs.AchievementType.*;
import static com.example.extraConfigs.HomeworkStatus.MAX_GRADE;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserRepository  userRepository;
    private final CourseRepository  courseRepository;
    private final HomeworkRepository homeworkRepository;

    public AchievementService(AchievementRepository achievementRepository, UserRepository userRepository, CourseRepository courseRepository, HomeworkRepository homeworkRepository) {
        this.achievementRepository = achievementRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.homeworkRepository = homeworkRepository;
    }
//закончил 1 курс
  public void addAchievementAfterEndingFirstCourse(Long userId, Course courseId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (user.get().getCompletedCourses().size() == 1){
                Achievement achievement = new Achievement();
                achievement.setName("First Course Completed");
                achievement.setRarity(COMMON);
                achievement.setDescription("Congratulations! You've completed your first course. Keep up the great work!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }

        }
  }
  //закончил 10 курсов
  public void completingTenCourse(Long userId, Course courseId){

        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (user.get().getCompletedCourses().size() == 10){
                Achievement achievement = new Achievement();
                achievement.setName("Ten Courses Completed");
                achievement.setRarity(LEGENDARY);
                achievement.setDescription("Congratulations! You've completed your first course. Keep up the great work!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }

        }
  }
//закончил 25 курсов
  public void completingTwentyFiveCourse(Long userId, Course courseId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (user.get().getCompletedCourses().size() == 25){
                Achievement achievement = new Achievement();
                achievement.setName("Twenty Five Courses Completed");
                achievement.setRarity(EPIC);
                achievement.setDescription("Congratulations! You've completed your first course. Keep up the great work!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }

        }
  }
  //первая кровь - сделать первое дз на 100балов
  public void toDoFirstHomeworkWell(Long userId, Course courseId){
      Optional<User> user = userRepository.findById(userId);
      Optional<Course> course = courseRepository.findById(courseId.getId());
      Optional<Homework> homework = homeworkRepository.findById(courseId.getId());

      if (user.isPresent() && course.isPresent() && homework.isPresent()) {
          if(homework.get().getStatus() == MAX_GRADE){
              Achievement achievement = new Achievement();
              achievement.setName("First Homework On Max Grade Completed");
              achievement.setRarity(COMMON);
              achievement.setDescription("Congratulations! You've completed your first homework on max grade. Keep up the great work!");
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
    //сделать 5 дз за 24часа
    public void toDoFiveHomeworksInOneDay(Long userId, Course courseId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());
        Optional<Homework> homework = homeworkRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (homework.isPresent() && isSubmittedWithin24Hours(homework.get())) {
                System.out.println("The homework was completed in 24 hours!");
            } else {
                System.out.println("Homework was submitted later than 24 hours.");
            }
            if(homework.get().getStatus() == MAX_GRADE){
                Achievement achievement = new Achievement();
                achievement.setName("Five Homeworks In One Day Completed");
                achievement.setRarity(COMMON);
                achievement.setDescription("Congratulations! You've completed your first homework on max grade. Keep up the great work!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());

            }
        }
    }
    //сделать дз за несколько попыток
    public void tryToDoOneHomeworkFewTimes(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());
        Optional<Homework> homework = homeworkRepository.findById(courseId.getId());

        if (user.isPresent() && course.isPresent()) {
            if (homework.isPresent() && homework.get().getCountingTries() <= 3) {
                Achievement achievement = new Achievement();
                achievement.setName("Nerves of steel - strength");
                achievement.setRarity(RARE);
                achievement.setDescription("Nerves of steel - strength. Don't give up!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());

            }

        }
    }


}
