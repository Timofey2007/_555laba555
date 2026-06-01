package org.example._555laba555;

import org.example._555laba555.cli.CommandHandler;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.service.ServiceManager;

/**
 * Главный класс приложения.
 * При запуске программа проверяет аргументы командной строки:
 */
public class Main {

    /** Имя файла для сохранения данных по умолчанию */
    private static final String DEFAULT_DATA_FILE = "records.csv";

    /**
     * Точка входа в программу.
     */
    public static void main(String[] args) {
        // Парсим аргументы командной строки
        String dataFile = parseDataFile(args);

        // Создаем менеджер сервисов (общий для всех режимов)
        ServiceManager services = new ServiceManager();

        // Создаем менеджер сохранения
        Conservation storage = new Conservation(dataFile);

        // Загружаем данные из файла при старте
        try {
            storage.load(services);
            System.out.println("Данные загружены из: " + dataFile);
        } catch (Exception e) {
            System.out.println("Начинаем с пустыми данными");
        }

        System.out.println("Запуск консольного интерфейса...");
        CommandHandler handler = new CommandHandler(services, storage);
        handler.run();

        // Сохраняем данные перед выходом
        try {
            storage.save(services);
            System.out.println("Данные сохранены в: " + dataFile);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения данных: " + e.getMessage());
        }

        // Сохраняем пользователей
        services.saveUsers();
    }

    /**
     * Парсит путь к файлу данных из аргументов.
     *
     * @param args аргументы командной строки
     * @return путь к файлу или значение по умолчанию
     */
    private static String parseDataFile(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--file")) {
                return args[i + 1];
            }
        }
        return DEFAULT_DATA_FILE;
    }
}