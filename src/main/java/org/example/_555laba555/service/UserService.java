package org.example._555laba555.service;

import org.example._555laba555.dataBase.UserRepository;
import org.example._555laba555.domain.User;
import org.example._555laba555.utils.ForPasswords;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final UserRepository repository;
    private final Map<Long, User> cache = new HashMap<>();
    private User currentUser = null;

    public UserService(UserRepository repository) {
        this.repository = repository;
        if (repository != null) {
            loadFromDatabase();
        }
    }

    private void loadFromDatabase() {
        try {
            cache.clear();
            for (User u : repository.findAll()) {
                cache.put(u.getId(), u);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки пользователей из БД: " + e.getMessage());
        }
    }

    public boolean register(String login, String password) {
        try {
            if (login == null || login.trim().isEmpty()) {
                System.out.println("Ошибка: логин не может быть пустым");
                return false;
            }
            if (password == null || password.trim().isEmpty()) {
                System.out.println("Ошибка: пароль не может быть пустым");
                return false;
            }
            if (findByLogin(login) != null) {
                System.out.println("Пользователь с логином '" + login + "' уже существует");
                return false;
            }

            User user = new User();
            user.setLogin(login.trim());
            user.setPasswordHash(ForPasswords.hashPassword(password));
            user.setCreatedAt(Instant.now());

            if (cache.isEmpty()) {
                user.setRole("ADMIN");
                System.out.println("Вы зарегистрированы как АДМИНИСТРАТОР");
            }

            if (repository != null) {
                repository.insert(user);
                cache.put(user.getId(), user);
            } else {
                user.setId(cache.size() + 1);
                cache.put(user.getId(), user);
            }

            saveUsers();

            System.out.println("Пользователь '" + login + "' зарегистрирован");
            return true;
        } catch (SQLException e) {
            if (e.getSQLState() != null && e.getSQLState().equals("23505")) {
                System.out.println("Ошибка: логин уже занят");
            } else {
                System.out.println("Ошибка БД: " + e.getMessage());
            }
            return false;
        }
    }

    public boolean login(String login, String password) {
        if (isAuthenticated()) {
            System.out.println("Вы уже авторизованы");
            return false;
        }

        User user = findByLogin(login);
        if (user == null) {
            System.out.println("Пользователь не найден");
            return false;
        }

        if (ForPasswords.checkPassword(password, user.getPasswordHash())) {
            currentUser = user;
            user.setLastLogin(Instant.now());
            if (repository != null) {
                try {
                    repository.update(user);
                    cache.put(user.getId(), user);
                } catch (SQLException e) {
                    System.err.println("Ошибка обновления lastLogin: " + e.getMessage());
                }
            }

            // СОХРАНЯЕМ ПОСЛЕ ВХОДА
            saveUsers();

            System.out.println("Добро пожаловать, " + login );
            return true;
        } else {
            System.out.println("Неверный пароль");
            return false;
        }
    }

    public void logout() {
        if (currentUser == null) {
            System.out.println("Вы не авторизованы");
            return;
        }
        System.out.println("До свидания, " + currentUser.getLogin());
        currentUser = null;
    }


    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
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


    public User findByLogin(String login) {
        for (User u : cache.values()) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                return u;
            }
        }
        return null;
    }


    public void saveUsers() {
        if (repository == null) {
            try {
                org.example._555laba555.fileManager.UserStorage userStorage =
                        new org.example._555laba555.fileManager.UserStorage();
                userStorage.saveUsers(cache);
            } catch (Exception e) {
                System.err.println("Ошибка сохранения пользователей в файл: " + e.getMessage());
            }
        } else {
            System.out.println("Пользователи сохранены в БД");
        }
    }
}