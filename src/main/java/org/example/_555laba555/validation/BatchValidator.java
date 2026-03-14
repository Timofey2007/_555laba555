package org.example._555laba555.validation;

import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.BatchStatus;

/**
 * Проверяет корректность данных партии реактива.
 * Выполняет проверки метки, количества, местоположения и других полей.
 */
public class BatchValidator {
    public static final int MAX_LABEL_LENGTH = 64;

    public static final int MAX_LOCATION_LENGTH = 64;

    /**
     * Проверяет партию на соответствие всем требованиям.
     */
    public static void validate(ReagentBatch batch) {
        if (batch == null) {
            throw new ValidationException("Партия не может быть пустой");
        }

        if (batch.getReagentId() <= 0) {
            throw new ValidationException("ID реактива должен быть положительным числом");
        }

        String label = batch.getLabel();
        if (label == null || label.trim().isEmpty()) {
            throw new ValidationException("Метка не может быть пустой");
        }
        if (label.length() > MAX_LABEL_LENGTH) {
            throw new ValidationException("Метка слишком длинная (макс. " + MAX_LABEL_LENGTH + ")");
        }

        if (batch.getQuantityCurrent() < 0) {
            throw new ValidationException("Количество не может быть отрицательным");
        }

        if (batch.getUnit() == null) {
            throw new ValidationException("Единицы измерения должны быть указаны");
        }

        String location = batch.getLocation();
        if (location == null || location.trim().isEmpty()) {
            throw new ValidationException("Местоположение не может быть пустым");
        }
        if (location.length() > MAX_LOCATION_LENGTH) {
            throw new ValidationException("Местоположение слишком длинное (макс. " + MAX_LOCATION_LENGTH + ")");
        }

        if (batch.getStatus() == null) {
            batch.setStatus(BatchStatus.ACTIVE);
        }
    }
}