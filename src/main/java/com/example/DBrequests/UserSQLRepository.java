package com.example.DBrequests;


import com.example.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserSQLRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserSQLRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Метод для создания пользователя
    public User createUser(User user) {
        String sql = "INSERT INTO user (name, email, address, phone, password, is_active, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getAddress(), user.getPhone(),
                user.getPassword(), user.getIsActive(), user.getRole());
        return findUserByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("User not saved"));
    }

    // Метод для поиска пользователя по ID
    public Optional<User> findUserById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, userRowMapper()).stream().findFirst();
    }

    // Метод для обновления профиля пользователя
    public int updateUserProfile(Long id, User user) {
        String sql = "UPDATE user SET name = ?, address = ?, phone = ? WHERE id = ?";
        return jdbcTemplate.update(sql, user.getName(), user.getAddress(), user.getPhone(), id);
    }

    // Метод для изменения пароля пользователя
    public int changeUserPassword(Long id, String password) {
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        return jdbcTemplate.update(sql, password, id);
    }

    // Вспомогательный метод для поиска пользователя по email
    public Optional<User> findUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, userRowMapper()).stream().findFirst();
    }

    // RowMapper для преобразования результата SQL-запроса в объект User
    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setAddress(rs.getString("address"));
            user.setPhone(rs.getString("phone"));
            user.setPassword(rs.getString("password"));
            user.setIsActive(rs.getBoolean("is_active"));
            user.setRole(rs.getString("role"));
            return user;
        };
    }
}