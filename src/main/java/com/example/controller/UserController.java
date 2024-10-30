package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update-profile/{id}")
    public ResponseEntity<User> changeUserProfile(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.changeUserProfile(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<User> changeUserPassword(@PathVariable Long id, @RequestBody String password) {
        User updatedUser = userService.changeUserPassword(id, password);
        return ResponseEntity.ok(updatedUser);
    }
}
