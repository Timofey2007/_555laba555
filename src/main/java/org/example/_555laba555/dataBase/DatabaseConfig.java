package org.example._555laba555.dataBase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input != null) {
                props.load(input);
                System.out.println("Конфигурация БД загружена из db.properties");
            } else {
                System.out.println("Файл db.properties не найден, используем переменные окружения");
                props.setProperty("db.url", System.getenv("DB_URL"));
                props.setProperty("db.user", System.getenv("DB_USER"));
                props.setProperty("db.password", System.getenv("DB_PASSWORD"));
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки конфигурации БД: " + e.getMessage());
        }
    }

    public static String getDbUrl() {
        return props.getProperty("db.url", "jdbc:postgresql://postgrepro.dc-edu.ru:5432/dbstud");
    }

    public static String getDbUser() {
        return props.getProperty("db.user", "bk_502775_2026");
    }

    public static String getDbPassword() {
        return props.getProperty("db.password", "bk_502775");
    }
}