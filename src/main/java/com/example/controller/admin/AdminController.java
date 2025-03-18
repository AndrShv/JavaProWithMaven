package com.example.controller.admin;

import com.example.dto.response.AuthResponse;
import com.example.model.User;
import com.example.repository.users.UserRepository;
import com.example.security.jwt.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

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
    @PostMapping("/make-teacher/{userId}")
    public ResponseEntity<?> makeTeacher(@PathVariable Long userId, @AuthenticationPrincipal UserDetails currentUser) {
        if (!currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied: You do not have permission to perform this action.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole().contains("TEACHER")) {
            return ResponseEntity.badRequest().body("User is already a teacher");
        }
        user.setRole("TEACHER");
        userRepository.save(user);
        String jwt = jwtTokenUtil.generateToken(user.getUsername(), List.of(user.getRole()));
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}