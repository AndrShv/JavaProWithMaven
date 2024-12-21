package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordEncoder extends JpaRepository<UserRepository, Long>{
}
