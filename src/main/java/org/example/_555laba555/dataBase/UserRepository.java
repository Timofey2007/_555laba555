package org.example._555laba555.dataBase;

import org.example._555laba555.domain.User;
import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    public void save(User user) {
        String sql = "INSERT INTO users (login, password_hash, role) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getRole());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при регистрации пользователя: " + e.getMessage());
        }
    }

    public Map<Long, User> loadAll() {
        Map<Long, User> users = new HashMap<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DataBaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User(rs.getLong("id"), rs.getString("login"), rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                users.put(user.getId(), user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка загрузки пользователей");
        }
        return users;
    }

    public void updateLastLogin(long userId, Instant lastLogin) {
        String sql = "UPDATE users SET last_login = ? WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.from(lastLogin));
            pstmt.setLong(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Не удалось обновить время последнего входа в БД");
        }
    }

    public User findByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(rs.getLong("id"), rs.getString("login"), rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя");
        }
        return null;
    }
}