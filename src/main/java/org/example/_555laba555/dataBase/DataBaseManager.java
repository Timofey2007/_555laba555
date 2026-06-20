package org.example._555laba555.dataBase;

import org.example._555laba555.validation.StorageException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseManager {

    public static Connection getConnection() throws StorageException {
        try {
            String url = DatabaseConfig.getDbUrl();
            String user = DatabaseConfig.getDbUser();
            String password = DatabaseConfig.getDbPassword();

            System.out.println("Попытка подключения к: " + url);
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение к постгресу успешно");
            return conn;
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к БД: " + e.getMessage());
            throw new StorageException("Не удалось подключиться к базе данных. Проверьте настройки в db.properties");
        }
    }
}