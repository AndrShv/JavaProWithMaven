package com.example.service.users;

import com.example.model.User;
import com.example.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Qualifier("securityPasswordEncoder")
    private final PasswordEncoder passwordEncoder;

    public void register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует: " + user.getUsername());
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Шифруем пароль перед сохранением
        System.out.println("Сохраняем пользователя: " + user.getUsername());
        userRepository.save(user);
        System.out.println("Пользователь сохранен успешно: " + user.getUsername());
    }
}

