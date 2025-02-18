package com.example.repository;

import com.example.model.Leaderboard;
import com.example.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    // Получить все записи лидеров
    List<Leaderboard> findAll();

    // Получить текущий ранг пользователя
    Optional<Leaderboard> findByUser(User user);

    // Обновить ранг пользователя
    <S extends Leaderboard> S save(S leaderboard);

    // Удалить запись о пользователе из лидерборда
    void deleteById(Long id);
}
