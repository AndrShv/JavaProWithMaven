package com.example.controller.achievements;

import com.example.model.Studing.Achievement;
import com.example.model.User.UserAchievement;
import com.example.service.studying.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public List<UserAchievement> getAllAchievements() {
        return achievementService.getAllAchievements();
    }

    @GetMapping("/{id}")
    public UserAchievement getAchievementById(@PathVariable Long id) {
        return achievementService.getAchievementById(id);
    }

    @PostMapping
    public Achievement createAchievement(@RequestBody Achievement achievement) {
        return achievementService.createAchievement(achievement);
    }

    @PostMapping("/users/{userId}/achievements/{achievementId}")
    public void assignAchievementToUser(@PathVariable Long userId, @PathVariable Long achievementId) {
        achievementService.assignAchievementToUser(userId, achievementId);
    }
}
