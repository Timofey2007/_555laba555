package org.example._555laba555.validation;

/**
 * Исключение, выбрасываемое при ошибках валидации данных.
 * Содержит понятное сообщение для пользователя о том, что именно не так.
 *
 * @author Студент
 * @version 1.0
 */
public class ValidationException extends RuntimeException {

    /**
     * Создает исключение с сообщением об ошибке.
     *
     * @param message описание ошибки
     */
    public ValidationException(String message) {
        super(message);
    }
}