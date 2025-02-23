package com.example.service.achievements;

import com.example.extraConfigs.CourseTheme;
import com.example.extraConfigs.HomeworkStatus;
import com.example.model.*;
import com.example.repository.achievement.AchievementRepository;
import com.example.repository.praises.PraiseRepository;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkRepository;
import com.example.repository.users.UserRepository;
import org.apache.http.client.UserTokenHandler;
import org.checkerframework.checker.units.qual.A;
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
    private final UserRepository  userRepository;
    private final CourseRepository  courseRepository;
    private final HomeworkRepository homeworkRepository;

    public AchievementService(AchievementRepository achievementRepository, PraiseRepository praiseRepository, UserRepository userRepository, CourseRepository courseRepository, HomeworkRepository homeworkRepository) {
        this.achievementRepository = achievementRepository;
        this.praiseRepository = praiseRepository;
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
                    achievement.setDescription("Congratulations! You've completed your first homework on max grade. Keep up the great work!");
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
    //закончи 3 разных круса, разных направлений
    public void finishThreeDifferentCourse(Long userId, Course courseId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());
         if(user.isPresent() && course.isPresent()){
                 Set<CourseTheme> uniqueThemes = user.get().getCompletedCourses().stream()
                         .map(Course::getTheme)
                         .collect(Collectors.toSet());
             if(user.get().getCompletedCourses().size() >= 3){
                 Achievement achievement = new Achievement();
                 achievement.setName("Polyglot");
                 achievement.setRarity(RARE);
                 achievement.setDescription("Polyglot - you know many themes!");
                 user.get().getAchievements().add(achievement);
                 userRepository.save(user.get());

             }
        }

    }
    //Student of the Year – Complete the course without making a single mistake on the tests
    public void completeCourseWithoutMistakes(Long userId, Course courseId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());
        if(user.isPresent() && course.isPresent()){
            if(user.get().getCompletedCourses().size() == 1){
                Achievement achievement = new Achievement();
                achievement.setName("Student of the Year");
                achievement.setRarity(LEGENDARY);
                achievement.setDescription("Student of the Year - you completed the course without making a single mistake on the tests!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    //Master of the Course – Complete the course without making a single mistake on the tests
    public void completeCourseWithoutMistakesMaster(Long userId, Course courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());
        Optional<Homework> homework = homeworkRepository.findByUserAndCourse(user.get(), course.get());

        if (user.isPresent() && course.isPresent() && homework.isPresent()) {
            if (course.get().isPassed() && homework.get().getMistakes() == 0 && homework.get().getCountingTries() == 1 && homework.get().getStatus() == MAX_GRADE) {
                if (user.get().getCompletedCourses().add(course.get())) {
                    Achievement achievement = new Achievement();
                    achievement.setName("Master of the Course");
                    achievement.setRarity(LEGENDARY);
                    achievement.setDescription("Master of the Course - you completed the course without making a single mistake on the tests!");
                    user.get().getAchievements().add(achievement);
                    userRepository.save(user.get());
                }
            }
        }
    }// получить 10 разных достижений
    public void getTenDifferentAchievements(Long  userId, Course courseId){
        Optional<User> user  = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if(user.isPresent() && course.isPresent()){
            if(user.get().getAchievements().size() == 10){
                Achievement achievement = new Achievement();
                achievement.setName("Elite Scholar");
                achievement.setRarity(EPIC);
                achievement.setDescription("Ten Different Achievements!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    //получить 25 разных достижений
    public void getTwentyFiveDifferentAchievements(Long  userId, Course courseId){
        Optional<User> user  = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());

        if(user.isPresent() && course.isPresent()){
            if(user.get().getAchievements().size() == 25){
                Achievement achievement = new Achievement();
                achievement.setName("Legendary Scholar");
                achievement.setRarity(LEGENDARY);
                achievement.setDescription("Twenty Five Different Achievements!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

    //получить все достижения
    public void getAllAchievements(Long  userId, Course courseId){
        Optional<User> user  = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId.getId());


        if(user.isPresent() && course.isPresent()){
            if(user.get().getAchievements().size() == user.get().getMAX_ACHIEVEMENTS()){
                Achievement achievement = new Achievement();
                achievement.setName("True Legend");
                achievement.setRarity(ULTIMATE);
                achievement.setDescription("All Achievements!");
                user.get().getAchievements().add(achievement);
                userRepository.save(user.get());
            }
        }
    }

//любимчик учителя
    public void checkTeacherFavorite(User student) {
        if (praiseRepository.existsByStudent(student)) {
            Achievement achievement = new Achievement();
            achievement.setName("Teacher’s Favorite");
            achievement.setRarity(EPIC);
            achievement.setDescription("Your efforts did not go unnoticed. Keep it up!");
            student.getAchievements().add(achievement);
            userRepository.save(student);
        }
    }
}
