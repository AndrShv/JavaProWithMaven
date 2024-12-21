package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomPasswordEncoder extends JpaRepository<User, Long> {
}
