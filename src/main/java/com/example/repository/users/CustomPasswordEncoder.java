package com.example.repository.users;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomPasswordEncoder extends JpaRepository<User, Long> {
}
