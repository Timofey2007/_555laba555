package org.example._555laba555.validation;

import org.example._555laba555.domain.Reagent;

/**
 * Проверяет корректность данных реактива перед сохранением.
 * Выполняет все проверки, описанные в задании: пустые значения, длину строк.
 */
public class ReagentValidator {
    public static final int MAX_NAME_LENGTH = 128;

    public static final int MAX_FORMULA_LENGTH = 32;

    public static final int MAX_CAS_LENGTH = 32;

    public static final int MAX_HAZARD_LENGTH = 32;

    /**
     * Проверяет реактив на соответствие всем требованиям.
     * Если данные некорректны, выбрасывает ValidationException.
     */
    public static void validate(Reagent reagent) {
        if (reagent == null) {
            throw new ValidationException("Реактив не может быть пустым");
        }

        String name = reagent.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Название слишком длинное (макс. " + MAX_NAME_LENGTH + ")");
        }

        String formula = reagent.getFormula();
        if (formula != null && formula.length() > MAX_FORMULA_LENGTH) {
            throw new ValidationException("Формула слишком длинная (макс. " + MAX_FORMULA_LENGTH + ")");
        }

        String cas = reagent.getCas();
        if (cas != null && cas.length() > MAX_CAS_LENGTH) {
            throw new ValidationException("CAS слишком длинный (макс. " + MAX_CAS_LENGTH + ")");
        }

        String hazard = reagent.getHazardClass();
        if (hazard != null && hazard.length() > MAX_HAZARD_LENGTH) {
            throw new ValidationException("Класс опасности слишком длинный (макс. " + MAX_HAZARD_LENGTH + ")");
        }
    }
}