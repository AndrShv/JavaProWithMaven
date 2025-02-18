package com.example.repository.users;

import com.example.model.User.User;
import com.example.model.User.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLevelRepository extends JpaRepository<UserLevel, Long> {
    Optional<UserLevel> findByUser(User user);

    <S extends UserLevel> S save(S userLevel);

    void deleteById(Long id);
}
