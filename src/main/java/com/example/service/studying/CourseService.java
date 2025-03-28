package com.example.service.studying;

import com.example.dto.request.CourseRequest;
import com.example.dto.response.CourseResponse;
import com.example.extraConfigs.AchievementType;
import com.example.extraConfigs.CourseTheme;
import com.example.extraConfigs.CourseWay;
import com.example.mappers.CourseMapper;
import com.example.model.*;
import com.example.rabbitMqConfigs.NotificationService;
import com.example.repository.achievement.AchievementRepository;
import com.example.repository.studying.AssignmentRepository;
import com.example.repository.studying.CourseRepository;
import com.example.repository.studying.HomeworkGradeRepository;
import com.example.repository.studying.HomeworkRepository;
import com.example.repository.users.UserRepository;
import com.example.service.achievements.AchievementCheckService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private HomeworkRepository  homeworkRepository;
    @Autowired
    private HomeworkGradeRepository homeworkGradeRepository;



    @Autowired
    private AchievementCheckService achievementCheckService;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public CourseResponse createCourse(CourseRequest request, String teacherUsername) {
        System.out.println("Start creating a teacher course: " + teacherUsername);

        User teacher = userRepository.findByUsername(teacherUsername)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacher(teacher);
        course.setTeacherEmail(teacher.getEmail());
        course.setStartedTime(LocalDateTime.now());

        if (request.getFinishedTime() != null) {
            try {
                course.setFinishedTime(LocalDateTime.parse(request.getFinishedTime()));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid finishedTime format", e);
            }
        } else {
            throw new IllegalArgumentException("finishedTime cannot be null");
        }

        if (request.getTheme() != null && request.getWay() != null) {
            try {
                course.setTheme(CourseTheme.valueOf(request.getTheme()));
                course.setWay(CourseWay.valueOf(request.getWay()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid theme or way value", e);
            }
        } else {
            throw new IllegalArgumentException("Theme and Way cannot be null");
        }
        Course savedCourse = courseRepository.save(course);

        if (teacher.getCourses().isEmpty()) {
            Achievement achievement = new Achievement();
            achievement.setName("Created Course: " + savedCourse.getTitle());
            achievement.setRarity(AchievementType.COMMON);
            achievement.setDescription("You have created a new course.");
            Set<User> userSet = new HashSet<>();
            userSet.add(teacher);
            achievement.setUsers(userSet);
            achievementRepository.save(achievement);
            teacher.getAchievements().add(achievement);
            userRepository.save(teacher);
        }


        System.out.println("Course successfully saved: " + savedCourse.getTitle());

        CourseResponse response = courseMapper.toResponse(savedCourse);
        response.setStartedTime(savedCourse.getStartedTime());
        response.setFinishedTime(savedCourse.getFinishedTime());
        response.setTheme(savedCourse.getTheme().toString());
        response.setWay(savedCourse.getWay().toString());
        String notificationText = "A new course has been created: " + savedCourse.getTitle();
        notificationService.sendAsyncNotification(teacher.getEmail(), "New Course", notificationText);
        System.out.println("Notification sent");

        return response;
    }
    @Transactional
    public void addUserToCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

        if (course.getStudents().contains(user)) {
            throw new IllegalStateException("User is already enrolled in this course");
        }
        course.getStudents().add(user);

        // Проверка: это первый студент в курсе?
        if (course.getStudents().size() == 1) {
            User teacher = course.getTeacher();
            Achievement firstStudentAchievement = new Achievement();
            firstStudentAchievement.setName("Your First Student Is Here!");
            firstStudentAchievement.setRarity(AchievementType.COMMON);
            firstStudentAchievement.setDescription("You have added a first student to your course.");
            firstStudentAchievement.setUsers(new HashSet<>(Set.of(teacher)));
            achievementRepository.save(firstStudentAchievement);
            teacher.getAchievements().add(firstStudentAchievement);
            userRepository.save(teacher);
            System.out.println("Achievement saved for teacher: " + teacher.getUsername());
        }

        // Проверка: это первый курс студента?
        if (user.getCourses() == null || user.getCourses().isEmpty()) {
            Achievement studentAchievement = new Achievement();
            studentAchievement.setName("You Joined Your First Course!");
            studentAchievement.setRarity(AchievementType.RARE);
            studentAchievement.setDescription("You have joined your first course.");
            studentAchievement.setUsers(new HashSet<>(Set.of(user)));
            achievementRepository.save(studentAchievement);
            user.getAchievements().add(studentAchievement);
            userRepository.save(user);
            System.out.println("Achievement saved for student: " + user.getUsername());
        }
        courseRepository.save(course);
    }



    public CourseResponse updateCourse(Long courseId, CourseRequest request, String teacherUsername) {
        System.out.println("Starting course update with ID: " + courseId + " for teacher: " + teacherUsername);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        if (!course.getTeacher().getUsername().equals(teacherUsername)) {
            System.out.println("Teacher is not authorized to update the course");
            throw new AccessDeniedException("You are not allowed to update this course");
        }
        if (request.getTitle() != null) {
            course.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            course.setDescription(request.getDescription());
        }

        if (request.getFinishedTime() != null) {
            try {
                course.setFinishedTime(LocalDateTime.parse(request.getFinishedTime()));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid finishedTime format", e);
            }
        }

        if (course.getStartedTime() != null) {
            course.setStartedTime(course.getStartedTime());
        }

        if (request.getTheme() != null && request.getWay() != null) {
            try {
                course.setTheme(CourseTheme.valueOf(request.getTheme()));
                course.setWay(CourseWay.valueOf(request.getWay()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid theme or way value", e);
            }
        }

        Course updatedCourse = courseRepository.save(course);
        System.out.println("Course successfully updated: " + updatedCourse.getTitle());
        CourseResponse response = courseMapper.toResponse(updatedCourse);
        response.setStartedTime(updatedCourse.getStartedTime());
        response.setFinishedTime(updatedCourse.getFinishedTime());
        response.setTheme(updatedCourse.getTheme().toString());
        response.setWay(updatedCourse.getWay().toString());
        return response;
    }



    public void deleteCourse(Long courseId, String teacherUsername) {
        System.out.println("Starting course deletion with ID: " + courseId + " for teacher: " + teacherUsername);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        if (!course.getTeacher().getUsername().equals(teacherUsername)) {
            System.out.println("Teacher is not authorized to delete the course");
            throw new AccessDeniedException("You are not allowed to delete this course");
        }
        courseRepository.delete(course);
        System.out.println("Course successfully deleted: " + course.getTitle());
    }


    @Transactional
    public List<CourseResponse> getAllCourses() {
        System.out.println("Retrieving all courses");
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toResponseList(courses);
    }


    public CourseResponse getCourseById(Long courseId) {
        System.out.println("Retrieving course with ID: " + courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        System.out.println("Course found: " + course.getTitle());
        return courseMapper.toResponse(course);
    }
    @Transactional
    public void completeCourse(Long userId, Long courseId) {
        if (!isCourseCompletedByUser(userId, courseId)) {
            throw new IllegalStateException("The course cannot be completed until all assignments are submitted with a score of 80+");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (user.getCompletedCourses() == null) {
            user.setCompletedCourses(new HashSet<>());
        }
        user.getCompletedCourses().add(course);
        userRepository.save(user);

        System.out.println("User " + user.getUsername() + " has completed course " + course.getTitle());
        if (user.getCompletedCourses().size() == 1) {
            achievementCheckService.achievementAfterEndingFirstCourse(userId, courseId);
        }
        if (user.getCompletedCourses().size() == 5) {
            achievementCheckService.completingFiveCourses(userId, courseId);
        }
        List<Homework> homeworkList = homeworkRepository.findByCourseIdAndUserId(courseId, userId);
        if (homeworkList.isEmpty()) {
            throw new RuntimeException("Homework not found for course " + courseId + " and user " + userId);
        }
        boolean noMistakesAndSingleTry = homeworkList.stream()
                .allMatch(homework -> homework.getMistakes() == 0 && homework.getCountingTries() == 1);
        if (noMistakesAndSingleTry) {
            achievementCheckService.completeCourseWithoutMistakes(userId, courseId);
        }
    }


    @Transactional
    public void checkAchievementsForAllUsersInCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<User> students = course.getStudents();

        if (students.isEmpty()) {
            System.out.println("No students enrolled in this course.");
            return;
        }

        for (User student : students) {
            List<HomeworkGrade> homeworkGrades = homeworkGradeRepository.findByCourseIdAndStudentId(courseId, student.getId());
            if (homeworkGrades.isEmpty()) {
                System.out.println("Homework not found for student " + student.getUsername());
                continue;
            }

            boolean allGradesPassed = homeworkGrades.stream()
                    .allMatch(grade -> grade.getGrade() >= 80);

            if (allGradesPassed) {
                completeCourse(student.getId(), courseId);
                System.out.println("All assignments completed successfully for " + student.getUsername());
            } else {
                System.out.println("Not all assignments passed for " + student.getUsername());
            }
        }
    }

    public boolean isCourseCompletedByUser(Long userId, Long courseId) {
        List<HomeworkGrade> homeworkGrades = homeworkGradeRepository.findByCourseIdAndStudentId(courseId, userId);
        if (homeworkGrades.size() < 2) {
            return false;
        }
        for (HomeworkGrade grade : homeworkGrades) {
            if (grade.getGrade() < 80) {
                return false;
            }
        }
        return true;
    }
}
