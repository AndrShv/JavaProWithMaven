package com.example.service.studying;

import com.example.model.Studing.Achievement;
import com.example.model.User.User;
import com.example.model.User.UserAchievement;
import com.example.repository.studying.AchievementRepository;
import com.example.repository.users.UserAchievementRepository;
import com.example.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.courseDetails.AchievementStatus.RECIEVED;

@Service
@AllArgsConstructor
@Transactional
public class AchievementService {
    private UserRepository userRepository;

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;


    public List<UserAchievement> getAllAchievements() {
    return userAchievementRepository.findAll();

    }
    public UserAchievement getAchievementById(Long id) {
        return userAchievementRepository.findById(id).orElse(null);
    }

    public Achievement createAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    public void assignAchievementToUser(Long userId, Long achievementId) {
        Achievement achievement = getAchievementById(achievementId).getAchievement();
        User user = userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setStatus(RECIEVED);
        userAchievement.setDateEarned(LocalDateTime.now());

        userAchievementRepository.save(userAchievement);
    }
}