package org.example._555laba555.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Вспомогательный класс для чтения ввода пользователя с консоли.
 * Обрабатывает ошибки ввода и повторяет запрос при неверных данных.
 *
 * @author Студент
 * @version 1.0
 */
public class InputHelper {
    /** Ридер для чтения ввода с консоли */
    private final BufferedReader reader;

    /**
     * Создает помощник ввода с указанным ридером.
     *
     * @param reader ридер для чтения ввода
     */
    public InputHelper(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Читает строку с консоли.
     *
     * @param prompt приглашение для ввода
     * @param required true если строка обязательна
     * @return введенная строка
     * @throws IOException если ошибка ввода
     */
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
     *
     * @param prompt приглашение для ввода
     * @return введенная строка (может быть пустой)
     * @throws IOException если ошибка ввода
     */
    public String readOptional(String prompt) throws IOException {
        System.out.print(prompt);
        String line = reader.readLine();
        return line == null ? "" : line.trim();
    }

    /**
     * Читает целое число с консоли.
     * При ошибке повторяет запрос.
     *
     * @param prompt приглашение для ввода
     * @return введенное число
     * @throws IOException если ошибка ввода
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
     *
     * @param prompt приглашение для ввода
     * @return введенное число
     * @throws IOException если ошибка ввода
     */
    public double readDouble(String prompt) throws IOException {
        try {
            return Double.parseDouble(readString(prompt, true));
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число");
            return readDouble(prompt);
        }
    }

    /**
     * Читает выбор из диапазона чисел.
     *
     * @param prompt приглашение для ввода
     * @param min минимальное значение
     * @param max максимальное значение
     * @return выбранное число
     * @throws IOException если ошибка ввода
     */
    public int readChoice(String prompt, int min, int max) throws IOException {
        try {
            int value = Integer.parseInt(readString(prompt, true));
            if (value < min || value > max) {
                System.out.println("Ошибка: введите число от " + min + " до " + max);
                return readChoice(prompt, min, max);
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число");
            return readChoice(prompt, min, max);
        }
    }

    /**
     * Читает дату в формате ГГГГ-ММ-ДД.
     *
     * @param prompt приглашение для ввода
     * @return дата или null если ввод пустой
     * @throws IOException если ошибка ввода
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

    /**
     * Запрашивает подтверждение Y/N.
     *
     * @param prompt приглашение для ввода
     * @return true если пользователь ввел Y или y
     * @throws IOException если ошибка ввода
     */
    public boolean confirm(String prompt) throws IOException {
        String answer = readString(prompt + " (Y/N): ", true);
        return answer.equalsIgnoreCase("Y");
    }
}