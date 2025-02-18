package com.example.controller.achievements;

import com.example.model.Studing.Achievement;
import com.example.model.User.User;
import com.example.model.User.UserAchievement;
import com.example.repository.studying.AchievementRepository;
import com.example.repository.users.UserAchievementRepository;
import com.example.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.courseDetails.AchievementStatus.RECIEVED;


@RequiredArgsConstructor
@Service
public class UserAchievementService {

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    public UserAchievement assignAchievementToUser(Long userId, Long achievementId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found with id: " + achievementId));

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setStatus(RECIEVED);
        userAchievement.setDateEarned(LocalDateTime.now());

        return userAchievementRepository.save(userAchievement);
    }

    public Optional<UserAchievement> getUserAchievements(Long userId) {
        return userAchievementRepository.findById(userId);
    }
}

