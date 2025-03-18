package com.example.controller.achievement;

import com.example.model.Course;
import com.example.model.User;
import com.example.repository.studying.CourseRepository;
import com.example.service.achievements.AchievementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService achievementService;
    private final CourseRepository courseRepository;

    public AchievementController(AchievementService achievementService, CourseRepository courseRepository) {
        this.achievementService = achievementService;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/complete-first-course")
    public ResponseEntity<String> completeFirstCourse(@RequestParam Long userId, @RequestParam Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        return course.map(c -> {
            achievementService.addAchievementAfterEndingFirstCourse(userId, c);
            return ResponseEntity.ok("Achievement checked for completing the first course.");
        }).orElseGet(() -> ResponseEntity.badRequest().body("Course not found."));
    }

    @PostMapping("/complete-ten-courses")
    public ResponseEntity<String> completeTenCourses(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::completingTenCourse, "Achievement checked for completing ten courses.");
    }

    @PostMapping("/complete-homework-max")
    public ResponseEntity<String> completeHomeworkMax(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::toDoFirstHomeworkWell, "Achievement checked for completing homework with max grade.");
    }

    @PostMapping("/complete-twenty-five-courses")
    public ResponseEntity<String> completeTwentyFiveCourses(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::completingTwentyFiveCourse, "Achievement checked for completing twenty-five courses.");
    }

    @PostMapping("/five-homeworks-one-day")
    public ResponseEntity<String> fiveHomeworksInOneDay(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::toDoFiveHomeworksInOneDay, "Achievement checked for completing five homeworks in one day.");
    }

    @PostMapping("/nerves-of-steel")
    public ResponseEntity<String> nervesOfSteel(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::tryToDoOneHomeworkFewTimes, "Achievement checked for retrying homework a few times.");
    }

    @PostMapping("/three-different-courses")
    public ResponseEntity<String> threeDifferentCourses(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::finishThreeDifferentCourse, "Achievement checked for finishing three different courses.");
    }

    @PostMapping("/student-of-the-year")
    public ResponseEntity<String> studentOfTheYear(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::completeCourseWithoutMistakes, "Achievement checked for Student of the Year achievement.");
    }

    @PostMapping("/master-of-the-course")
    public ResponseEntity<String> masterOfTheCourse(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::completeCourseWithoutMistakesMaster, "Achievement checked for Master of the Course.");
    }

    @PostMapping("/ten-different-achievements")
    public ResponseEntity<String> tenDifferentAchievements(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::getTenDifferentAchievements, "Achievement checked for getting ten different achievements.");
    }

    @PostMapping("/twenty-five-different-achievements")
    public ResponseEntity<String> twentyFiveDifferentAchievements(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::getTwentyFiveDifferentAchievements, "Achievement checked for getting twenty-five different achievements.");
    }

    @PostMapping("/all-achievements")
    public ResponseEntity<String> allAchievements(@RequestParam Long userId, @RequestParam Long courseId) {
        return handleCourseRequest(userId, courseId, achievementService::getAllAchievements, "Achievement checked for obtaining all achievements.");
    }

    @PostMapping("/teacher-favorite")
    public ResponseEntity<String> teacherFavorite(@RequestParam Long userId) {
        achievementService.checkTeacherFavorite(userId);
        return ResponseEntity.ok("Achievement checked for Teacher’s Favorite.");
    }

    private ResponseEntity<String> handleCourseRequest(Long userId, Long courseId, BiConsumer<Long, Course> achievementMethod, String successMessage) {
        Optional<Course> course = courseRepository.findById(courseId);
        return course.map(c -> {
            achievementMethod.accept(userId, c);
            return ResponseEntity.ok(successMessage);
        }).orElseGet(() -> ResponseEntity.badRequest().body("Course not found."));
    }
}
