package org.example._555laba555.cli;

/**
 * Главный класс приложения.
 */
public class ReagentTracker {

    /**
     * Точка входа в программу.
     */
    public static void main(String[] args) {
        CommandHandler handler = new CommandHandler();
        handler.run();
    }
}