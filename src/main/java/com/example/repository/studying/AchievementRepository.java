package com.example.repository.studying;

import com.example.model.Studing.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    List<Achievement> findAll();
    Optional<Achievement> findById(Long id);

    Optional<Achievement> findByName(String name);

    <S extends Achievement> S save(S achievement);
    void deleteById(Long id);
}
