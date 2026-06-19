package org.example._555laba555.validation;

/**
 * Исключение, которое выбрасывается при ошибках работы с базой данных (JDBC).
 * Позволяет перехватывать ошибки подключения, нарушения уникальности и внешних ключей.
 */
public class StorageException extends RuntimeException { // Можно наследовать от Exception, но Runtime удобнее для UI

    /**
     * Создает исключение только с текстовым сообщением.
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и первоначальной причиной (например, SQLException).
     * Это позволяет сохранить лог ошибки для отладки [1].
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}