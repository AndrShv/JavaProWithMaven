package com.example.repository.studying;

import com.example.model.Studing.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
    List<Level> findAll();
    Optional<Level> findById(Long id);

    Optional<Level> findByLevelName(String levelName);
    Optional<Level> findFirstByMinExperienceLessThanEqualAndMaxExperienceGreaterThanEqual(Integer minExperience, Integer maxExperience);
    <S extends Level> S save(S level);
    void deleteById(Long id);
}
