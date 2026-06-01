package org.example._555laba555.service;

import org.example._555laba555.domain.User;
import org.example._555laba555.utils.ForPasswords;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private User currentUser = null;
    private long nextId = 1;

    public boolean register(String login, String password) {
        if (login == null || login.trim().isEmpty()) {
            System.out.println("ошибка: логин не может быть пустым");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("Ошибка: пароль не может быть пустым");
            return false;
        }
        if (findByLogin(login) != null) { // уникальность логина
            System.out.println("Ошибка: пользователь с логином '" + login + "' уже существует");
            return false;
        }
        User user = new User();
        user.setId(nextId++);
        user.setLogin(login.trim());
        user.setPasswordHash(ForPasswords.hashPassword(password));
        user.setCreatedAt(Instant.now());

        if (users.isEmpty()) {
            user.setRole("ADMIN");
            System.out.println("Вы зарегистрированы как первый пользователь (администратор)");
        }

        users.put(user.getId(), user);
        System.out.println("Пользователь '" + login + "' успешно зарегистрирован");
        return true;
    }
    public boolean login(String login, String password) {
        if (isAuthenticated()) {
            System.out.println("Вы уже авторизованы как '" + currentUser.getLogin() + "'");
            return false;
        }
        User user = findByLogin(login);
        if (user == null) {
            System.out.println("ошибка: пользователь с логином '" + login + "' не найден");
            return false;
        }
        if (ForPasswords.checkPassword(password, user.getPasswordHash())) {
            currentUser = user;
            user.setLastLogin(Instant.now());
            System.out.println("дарова, " + login );
            if (user.isAdmin()) {
                System.out.println("   У вас есть права администратора");
            }
            return true;
        } else {
            System.out.println("Ошибка: неверный пароль");
            return false;
        }
    }
    public void logout() {
        if (currentUser == null) {
            System.out.println("Вы не авторизованы");
            return;
        }
        System.out.println("Покеда, " + currentUser.getLogin());
        currentUser = null;
    }


    public boolean isAuthenticated() {
        return currentUser != null;
    }


    public boolean isAdmin() { // админ ли текущий пользователь
        return currentUser != null && currentUser.isAdmin();
    }


    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Получить ID текущего пользователя (или 0, если не авторизован)
     */
    public long getCurrentUserId() {
        return currentUser == null ? 0 : currentUser.getId();
    }


    public String getCurrentUserLogin() {
        return currentUser == null ? "не авторизован" : currentUser.getLogin();
    }


    public User findByLogin(String login) {
        for (User user : users.values()) {
            if (user.getLogin().equalsIgnoreCase(login)) {
                return user;
            }
        }
        return null;
    }

    public User getById(long id) {
        return users.get(id);
    }

    /**
     * Загрузить пользователей из Map (используется при загрузке из файла)
     */
    public void loadFromMap(Map<Long, User> loadedUsers) {
        users.clear();
        users.putAll(loadedUsers);
        // Обновляем nextId
        for (Long id : users.keySet()) {
            if (id >= nextId) {
                nextId = id + 1;
            }
        }
    }

    /**
     * Получить всех пользователей
     */
    public Map<Long, User> getAllUsers() {
        return new HashMap<>(users);
    }

    /**
     * Проверить, есть ли хотя бы один пользователь
     */
    public boolean isEmpty() {
        return users.isEmpty();
    }
}
