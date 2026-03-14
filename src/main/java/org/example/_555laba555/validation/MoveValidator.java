package org.example._555laba555.validation;

import org.example._555laba555.domain.StockMove;
import org.example._555laba555.domain.StockMoveType;

/**
 * Проверяет корректность данных движения.
 * Выполняет проверки количества, типа движения и причины.
 */
public class MoveValidator {
    /** Максимальная длина причины */
    public static final int MAX_REASON_LENGTH = 128;

    /**
     * Проверяет движение на соответствие основным требованиям.
     */
    public static void validate(StockMove move) {
        if (move == null) {
            throw new ValidationException("Движение не может быть пустым");
        }

        if (move.getBatchId() <= 0) {
            throw new ValidationException("ID партии должен быть положительным числом");
        }

        if (move.getType() == null) {
            throw new ValidationException("Тип движения должен быть указан");
        }

        if (move.getQuantity() <= 0) {
            throw new ValidationException("Количество должно быть положительным");
        }

        if (move.getUnit() == null) {
            throw new ValidationException("Единицы измерения должны быть указаны");
        }

        String reason = move.getReason();
        if (reason != null && reason.length() > MAX_REASON_LENGTH) {
            throw new ValidationException("Причина слишком длинная (макс. " + MAX_REASON_LENGTH + ")");
        }
    }

    /**
     * Проверяет, достаточно ли реактива для операции расхода или списания.
     */
    public static void checkQuantity(StockMove move, double currentQuantity) {
        if ((move.getType() == StockMoveType.OUT || move.getType() == StockMoveType.DISCARD)
                && move.getQuantity() > currentQuantity) {
            throw new ValidationException("Недостаточно реактива. Доступно: " + currentQuantity);
        }
    }
}