package com.example.repository.praises;

import com.example.model.Praise;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PraiseRepository extends JpaRepository<Praise, Long>{
    boolean existsByStudent(User student);
}
