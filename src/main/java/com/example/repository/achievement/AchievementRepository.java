package com.example.repository.achievement;

import com.example.model.Achievement;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByName(String name);
    boolean existsByUsersAndName(User user, String name);
}