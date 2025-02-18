package com.example.controller.achievements;

import com.example.model.User.UserAchievement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user-achievements")
public class UserAchievementController {

    @Autowired
    private UserAchievementService userAchievementService;

    @PostMapping("/assign")
    public ResponseEntity<UserAchievement> assignAchievement(@RequestParam Long userId, @RequestParam Long achievementId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userAchievementService.assignAchievementToUser(userId, achievementId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Optional<UserAchievement>> getUserAchievements(@PathVariable Long userId) {
        return ResponseEntity.ok(userAchievementService.getUserAchievements(userId));
    }
}
