package org.example._555laba555.fileManager;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.example._555laba555.domain.User;

import java.io.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserStorage {
    private static final String USER_FILE = "users.csv";

    public void saveUsers(Map<Long, User> users) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(USER_FILE))) {
            writer.writeNext(new String[]{"id", "login", "passwordHash", "role", "createdAt", "lastLogin"});

            for (User u : users.values()) {
                writer.writeNext(new String[]{
                        String.valueOf(u.getId()),
                        u.getLogin(),
                        u.getPasswordHash(),
                        u.getRole(),
                        u.getCreatedAt() != null ? u.getCreatedAt().toString() : "",
                        u.getLastLogin() != null ? u.getLastLogin().toString() : ""
                });
            }
            System.out.println("Пользователи сохранены в: " + USER_FILE);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения пользователей: " + e.getMessage());
        }
    }

    public Map<Long, User> loadUsers() {
        Map<Long, User> users = new HashMap<>();
        File file = new File(USER_FILE);

        if (!file.exists()) {
            System.out.println("Файл пользователей не найден, будет создан при сохранении");
            return users;
        }

        try (CSVReader reader = new CSVReader(new FileReader(USER_FILE))) {
            reader.skip(1);
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length < 4) continue;

                User u = new User();
                u.setId(Long.parseLong(line[0]));
                u.setLogin(line[1]);
                u.setPasswordHash(line[2]);
                u.setRole(line[3]);

                if (line.length > 4 && !line[4].isEmpty()) {
                    u.setCreatedAt(Instant.parse(line[4]));
                }
                if (line.length > 5 && !line[5].isEmpty()) {
                    u.setLastLogin(Instant.parse(line[5]));
                }

                users.put(u.getId(), u);
            }

            System.out.println("Загружено пользователей: " + users.size());
        } catch (Exception e) {
            System.err.println("Ошибка загрузки пользователей: " + e.getMessage());
        }

        return users;
    }
}