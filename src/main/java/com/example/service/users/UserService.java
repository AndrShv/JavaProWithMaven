package com.example.service.users;

import com.example.model.User;
import com.example.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Qualifier("securityPasswordEncoder")
    private final PasswordEncoder passwordEncoder;

    public void register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("A user with this username already exists: " + user.getUsername());
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Saving user: " + user.getUsername());
        System.out.println("Saving user: " + user.getEmail());
        System.out.println("Saving user: " + user.getPassword());
        System.out.println("Saving user: " + user.getId());
        userRepository.save(user);
        System.out.println("User saved successfully: " + user.getUsername());
        System.out.println("Saving user: " + user.getEmail());
        System.out.println("Saving user: " + user.getPassword());
        System.out.println("Saving user: " + user.getId());
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
