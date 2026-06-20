package org.example._555laba555.service;

import org.example._555laba555.domain.User;
import org.example._555laba555.utils.ForPasswords;
import org.example._555laba555.dataBase.UserRepository;
import org.example._555laba555.validation.StorageException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private User currentUser = null;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void loadAll() {
        users.clear();
        Map<Long, User> fromDb = userRepository.loadAll();
        users.putAll(fromDb);
    }

    public boolean register(String login, String password) {
        if (userRepository.findByLogin(login) != null) {
            return false;
        }
        User user = new User();
        user.setLogin(login);
        user.setPasswordHash(ForPasswords.hashPassword(password));
        user.setRole(users.isEmpty() ? "ADMIN" : "USER");
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
        users.put(user.getId(), user);
        return true;
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByLogin(username);
        if (user != null && ForPasswords.checkPassword(password, user.getPasswordHash())) {
            user.setLastLogin(Instant.now());
            userRepository.updateLastLogin(user.getId(), user.getLastLogin());
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public long getCurrentUserId() {
        return currentUser == null ? 0 : currentUser.getId();
    }

    public String getCurrentUserLogin() {
        return currentUser == null ? "не авторизован" : currentUser.getLogin();
    }
}