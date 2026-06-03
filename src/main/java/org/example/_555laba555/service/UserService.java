package org.example._555laba555.service;

import org.example._555laba555.domain.User;
import org.example._555laba555.fileManager.UserStorage;
import org.example._555laba555.utils.ForPasswords;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private User currentUser = null;
    private long nextId = 1;
    private final UserStorage userStorage;

    public UserService() {
        this.userStorage = new UserStorage();
        // ЗАГРУЖАЕМ ПОЛЬЗОВАТЕЛЕЙ ПРИ СОЗДАНИИ СЕРВИСА
        Map<Long, User> loadedUsers = userStorage.loadUsers();
        if (!loadedUsers.isEmpty()) {
            loadFromMap(loadedUsers);
        }
    }

    public boolean register(String login, String password) {
        if (login == null || login.trim().isEmpty()) {
            System.out.println("Ошибка: логин не может быть пустым");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            System.out.println("Ошибка: пароль не может быть пустым");
            return false;
        }

        if (findByLogin(login) != null) {
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

        // СОХРАНЯЕМ СРАЗУ ПОСЛЕ РЕГИСТРАЦИИ
        saveUsersToFile();

        return true;
    }

    public boolean login(String login, String password) {
        if (isAuthenticated()) {
            System.out.println("Вы уже авторизованы как '" + currentUser.getLogin() + "'");
            return false;
        }

        User user = findByLogin(login);
        if (user == null) {
            System.out.println("Ошибка: пользователь с логином '" + login + "' не найден");
            return false;
        }

        if (ForPasswords.checkPassword(password, user.getPasswordHash())) {
            currentUser = user;
            user.setLastLogin(Instant.now());
            System.out.println("Добро пожаловать, " + login + "!");
            if (user.isAdmin()) {
                System.out.println("У вас есть права администратора");
            }
            // СОХРАНЯЕМ ПОСЛЕ ОБНОВЛЕНИЯ lastLogin
            saveUsersToFile();
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
        System.out.println("покеда, " + currentUser.getLogin());
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

    public void loadFromMap(Map<Long, User> loadedUsers) {
        users.clear();
        users.putAll(loadedUsers);
        for (Long id : users.keySet()) {
            if (id >= nextId) {
                nextId = id + 1;
            }
        }
    }

    public Map<Long, User> getAllUsers() {
        return new HashMap<>(users);
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    /**
     * Сохранить пользователей в файл
     */
    public void saveUsersToFile() {
        userStorage.saveUsers(users);
    }
}