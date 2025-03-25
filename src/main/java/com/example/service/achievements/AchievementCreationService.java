package com.example.service.achievements;

import com.example.extraConfigs.AchievementType;
import com.example.model.Achievement;
import com.example.model.Homework;
import com.example.model.User;
import com.example.repository.users.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AchievementCreationService {
    private final UserRepository  userRepository;

    public AchievementCreationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Achievement createAchievement(String name, AchievementType rarity, String description) {
        Achievement achievement = new Achievement();
        achievement.setName(name);
        achievement.setRarity(rarity);
        achievement.setDescription(description);
        return achievement;
    }

    public void addAchievementToUser(Long userId, Achievement achievement) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().getAchievements().add(achievement);
            userRepository.save(user.get());
        }

    }

    public boolean isSubmittedWithin24Hours(Homework homework) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime submissionTime = homework.getDoneAtTime();
        Duration duration = Duration.between(submissionTime, currentTime);
        return duration.toHours() < 24;
    }
}
