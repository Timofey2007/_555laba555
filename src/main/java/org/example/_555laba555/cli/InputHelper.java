package org.example._555laba555.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Вспомогательный класс для чтения ввода пользователя с консоли.
 */
public class InputHelper {
    /**
     * Ридер для чтения ввода с консоли
     */
    private final BufferedReader reader;

    public InputHelper(BufferedReader reader) {
        this.reader = reader;
    }

    public String readString(String prompt, boolean required) throws IOException {
        System.out.print(prompt);
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Ввод прерван");
        }
        String trimmed = line.trim();
        if (required && trimmed.isEmpty()) {
            System.out.println("Ошибка: значение не может быть пустым");
            return readString(prompt, required);
        }
        return trimmed;
    }

    /**
     * Читает необязательную строку (может быть пустой).
     */
    public String readOptional(String prompt) throws IOException {
        System.out.print(prompt);
        String line = reader.readLine();
        return line == null ? "" : line.trim();
    }

    /**
     * Читает целое число с консоли.
     * При ошибке повторяет запрос.
     */
    public long readLong(String prompt) throws IOException {
        try {
            return Long.parseLong(readString(prompt, true));
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите целое число");
            return readLong(prompt);
        }
    }

    /**
     * Читает вещественное число с консоли.
     * При ошибке повторяет запрос.
     */
    public double readDouble(String prompt) throws IOException {
        try {
            return Double.parseDouble(readString(prompt, true));
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число");
            return readDouble(prompt);
        }
    }

    public int readInt(String prompt) throws IOException {
        try {
            return Integer.parseInt(readString(prompt, true));
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите целое число");
            return readInt(prompt);
        }
    }

    /**
     * Читает дату в формате ГГГГ-ММ-ДД.
     */
    public LocalDate readDate(String prompt) throws IOException {
        String text = readOptional(prompt);
        if (text.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: неверный формат даты. Используйте ГГГГ-ММ-ДД");
            return readDate(prompt);
        }
    }
}