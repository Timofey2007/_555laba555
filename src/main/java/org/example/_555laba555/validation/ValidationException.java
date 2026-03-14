package org.example._555laba555.validation;

/**
 * Исключение, выбрасываемое при ошибках валидации данных.
 * Содержит понятное сообщение для пользователя о том, что именно не так.
 */
public class ValidationException extends RuntimeException {

    /**
     * Создает исключение с сообщением об ошибке.
     */
    public ValidationException(String message) {
        super(message);
    }
}