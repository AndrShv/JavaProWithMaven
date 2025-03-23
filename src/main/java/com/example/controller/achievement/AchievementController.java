package com.example.controller.achievement;

import com.example.service.achievements.AchievementCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementCheckService achievementCheckService;

    public AchievementController(AchievementCheckService achievementCheckService) {
        this.achievementCheckService = achievementCheckService;
    }

    @PostMapping("/complete-first-course")
    public ResponseEntity<String> completeFirstCourse(@RequestParam Long userId, @RequestParam Long courseId) {
        achievementCheckService.checkAchievementOnCompleteCourse(userId, courseId);
        return ResponseEntity.ok("Achievements checked for completing a course.");
    }

    @PostMapping("/complete-homework-max")
    public ResponseEntity<String> completeHomeworkMax(@RequestParam Long userId, @RequestParam Long courseId) {
        achievementCheckService.toDoFirstHomeworkWell(userId, courseId);
        return ResponseEntity.ok("Achievement checked for completing homework with max grade.");
    }

    @PostMapping("/five-homeworks-one-day")
    public ResponseEntity<String> fiveHomeworksInOneDay(@RequestParam Long userId, @RequestParam Long courseId) {
        achievementCheckService.toDoFiveHomeworksInOneDay(userId, courseId);
        return ResponseEntity.ok("Achievement checked for completing five homeworks in one day.");
    }

    @PostMapping("/nerves-of-steel")
    public ResponseEntity<String> nervesOfSteel(@RequestParam Long userId, @RequestParam Long courseId) {
        achievementCheckService.tryToDoOneHomeworkFewTimes(userId, courseId);
        return ResponseEntity.ok("Achievement checked for retrying homework a few times.");
    }

    @PostMapping("/three-different-courses")
    public ResponseEntity<String> threeDifferentCourses(@RequestParam Long userId, @RequestParam Long courseId) {
        achievementCheckService.finishThreeDifferentCourses(userId, courseId);
        return ResponseEntity.ok("Achievement checked for finishing three different courses.");
    }

    @PostMapping("/student-of-the-year")
    public ResponseEntity<String> studentOfTheYear(@RequestParam Long userId, @RequestParam Long courseId) {
        achievementCheckService.completeCourseWithoutMistakes(userId, courseId);
        return ResponseEntity.ok("Achievement checked for Student of the Year.");
    }

    @PostMapping("/ten-different-achievements")
    public ResponseEntity<String> tenDifferentAchievements(@RequestParam Long userId) {
        achievementCheckService.getTenDifferentAchievements(userId);
        return ResponseEntity.ok("Achievement checked for getting ten different achievements.");
    }

    @PostMapping("/twenty-five-different-achievements")
    public ResponseEntity<String> twentyFiveDifferentAchievements(@RequestParam Long userId) {
        achievementCheckService.getTwentyFiveDifferentAchievements(userId);
        return ResponseEntity.ok("Achievement checked for getting twenty-five different achievements.");
    }

    @PostMapping("/all-achievements")
    public ResponseEntity<String> allAchievements(@RequestParam Long userId) {
        achievementCheckService.getAllAchievements(userId);
        return ResponseEntity.ok("Achievement checked for obtaining all achievements.");
    }

    @PostMapping("/teacher-favorite")
    public ResponseEntity<String> teacherFavorite(@RequestParam Long userId) {
        achievementCheckService.checkTeacherFavorite(userId);
        return ResponseEntity.ok("Achievement checked for Teacher’s Favorite.");
    }
}
