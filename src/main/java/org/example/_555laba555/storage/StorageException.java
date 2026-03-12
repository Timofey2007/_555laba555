package org.example._555laba555.storage;

/**
 * Исключение, выбрасываемое при ошибках работы с файловым хранилищем.
 * Например, когда файл не удается прочитать или записать.
 *
 * @author Студент
 * @version 1.0
 */
public class StorageException extends Exception {

    /**
     * Создает исключение с сообщением об ошибке.
     *
     * @param message описание ошибки
     */
    public StorageException(String message) {
        super(message);
    }

    /**
     * Создает исключение с сообщением и причиной.
     *
     * @param message описание ошибки
     * @param cause исходная причина (например, IOException)
     */
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}