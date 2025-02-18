package com.example.repository.users;

import com.example.model.Studing.Achievement;
import com.example.model.User.User;
import com.example.model.User.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {

    List<UserAchievement> findByUser(User user);
    List<UserAchievement> findByAchievement(Achievement achievement);

    Optional<UserAchievement> findByUserAndAchievement(User user, Achievement achievement);

    <S extends UserAchievement> S save(S userAchievement);


    void deleteById(Long id);
}

