package com.example.controller.admin;

import com.example.model.User.User;
import com.example.repository.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Service
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    @PutMapping("/change-role/{userId}")
    public void changeUserRole(Long userId, String newRole) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(newRole);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    @PutMapping("/make-admin/{userId}")
    public void makeUserAdmin(Long userId) {
        changeUserRole(userId, "ADMIN");
    }

    @Transactional
    @PutMapping("/make-teacher/{userId}")
    public void makeUserTeacher(Long userId) {
        changeUserRole(userId, "TEACHER");
    }
}
