package org.example._555laba555.cli;

/**
 * Главный класс приложения.
 * Запускает обработчик команд и начинает работу программы.
 *
 * @author Студент
 * @version 1.0
 */
public class ReagentTracker {

    /**
     * Точка входа в программу.
     * Создает обработчик команд и запускает его.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        CommandHandler handler = new CommandHandler();
        handler.run();
    }
}