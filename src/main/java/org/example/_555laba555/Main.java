package org.example._555laba555;

import org.example._555laba555.cli.CommandHandler;
import org.example._555laba555.service.ServiceManager;

public class Main {

    private static final String DEFAULT_DATA_FILE = "records.csv";



    public static void main(String[] args) {
        boolean useDatabase = false;
        String dataFile = DEFAULT_DATA_FILE;


        // Парсим аргументы
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--db")) {
                useDatabase = true;
            } else if (args[i].equals("--file") && i + 1 < args.length) {
                dataFile = args[i + 1];
                i++;
            }
        }

        // Создаём менеджер сервисов
        ServiceManager services = new ServiceManager(useDatabase);


        // Сохраняем при выходе
        services.saveAll();
    }
}