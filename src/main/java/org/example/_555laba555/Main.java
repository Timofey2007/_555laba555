package org.example._555laba555;

import org.example._555laba555.cli.CommandHandler;
import org.example._555laba555.ui.ReagentUI;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.service.ServiceManager;

/**
 * Главный класс приложения.
 * При запуске программа проверяет аргументы командной строки:
 * --ui  - запуск графического интерфейса JavaFX
 */
public class Main {

    /** Имя файла для сохранения данных по умолчанию */
    private static final String DEFAULT_DATA_FILE = "lab5_data.csv";

    /**
     * Точка входа в программу.
     *
     */
    public static void main(String[] args) {
        // Парсим аргументы командной строки
        String mode = parseMode(args);
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
            System.out.println("Предупреждение: не удалось загрузить данные - " + e.getMessage());
            System.out.println("Начинаем с пустыми данными");
        }

        // Запускаем выбранный режим
        switch (mode) {
            case "ui":
                // Запуск графического интерфейса JavaFX
                System.out.println("Запуск графического интерфейса...");
                ReagentUI.launch(ReagentUI.class, services, storage, dataFile);
                break;
            default:
                // Запуск консольного интерфейса
                System.out.println("Запуск консольного интерфейса...");
                CommandHandler handler = new CommandHandler(services, storage);
                handler.run();
                break;
        }

        // Сохраняем данные перед выходом (на всякий случай)
        try {
            storage.save(services);
            System.out.println("Данные сохранены в: " + dataFile);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения при выходе: " + e.getMessage());
        }
    }

    /**
     * Парсит режим запуска из аргументов.
     *
     * @param args аргументы командной строки
     * @return "ui" для графического режима,
     */
    private static String parseMode(String[] args) {
        for (String arg : args) {
            if (arg.equals("--ui")) {
                return "ui";
            }
        }
        return "";
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