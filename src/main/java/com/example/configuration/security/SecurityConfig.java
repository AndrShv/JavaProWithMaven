package com.example.configuration.security;

import com.example.security.jwt.JwtAuthenticationFilter;
import com.example.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;

    @Lazy
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем доступ без авторизации к определённым маршрутам
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/courses/all").permitAll()  // Разрешаем доступ к этому маршруту без авторизации
                        .requestMatchers("/api/v1/notification/send").permitAll()
                        .requestMatchers("/api/send-message").permitAll()
                        .requestMatchers("/api/lessons/**").authenticated() // Разрешить всем
                        // Требуем авторизацию для добавления пользователей и других операций с курсами
                        .requestMatchers("/api/courses/add-user").hasAuthority("ROLE_TEACHER")
                        .requestMatchers("/api/courses/user/**").authenticated()
                        .requestMatchers("/api/courses/**").authenticated()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        // Для всех остальных запросов — нужно быть авторизованным
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(Collections.singletonList(authProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenUtil, userDetailsService);
    }
}