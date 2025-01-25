package com.example.controller.auth;

import com.example.dto.response.AuthResponse;
import com.example.dto.request.LoginRequest;
import com.example.dto.request.RegisterRequest;
import com.example.model.User;
import com.example.security.jwt.JwtTokenUtil;
import com.example.service.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null || request.getRole() == null || request.getEmail() == null) {
            return ResponseEntity.badRequest().body("Все поля обязательны");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setEmail(request.getEmail());
        userService.register(user);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // Проверяем, указан ли username или email
        String loginIdentifier = request.getUsername() != null ? request.getUsername() : request.getEmail();
        if (loginIdentifier == null) {
            return ResponseEntity.badRequest().body("Необходимо указать имя пользователя или email");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginIdentifier, request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateToken(loginIdentifier);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
