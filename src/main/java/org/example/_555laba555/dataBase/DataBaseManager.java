package org.example._555laba555.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseManager {
    public static Connection getConnection()  {
        try {
            String url = DatabaseConfig.getDbUrl();
            String user = DatabaseConfig.getDbUser();
            String password = DatabaseConfig.getDbPassword();

            System.out.println("Попытка подключения к: " + url);
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение успешно!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            throw new RuntimeException("Не удалось подключиться к базе данных. Проверьте настройки в db.properties");
        }
    }
}