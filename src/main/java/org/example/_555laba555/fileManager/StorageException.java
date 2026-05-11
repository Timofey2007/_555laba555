package org.example._555laba555.fileManager;

/**
 * Исключение, которое выбрасывается при ошибках работы с хранилищем данных.
 * Используется для отделения ошибок ввода-вывода от бизнес-логики.
 * Сообщение исключения понятно пользователю и не содержит stacktrace.
 */
public class StorageException extends Exception {

    /**
     * Создает исключение с сообщением об ошибке.
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}