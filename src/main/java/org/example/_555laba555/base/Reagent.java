package org.example._555laba555.base;

import java.time.Instant;
import java.util.Objects;

/**
 * Реактив (вещество).
 */
public final class Reagent {
    // Константы для валидации
    public static final int MAX_NAME_LENGTH = 128;
    public static final int MAX_FORMULA_LENGTH = 32;
    public static final int MAX_CAS_LENGTH = 32;
    public static final int MAX_HAZARD_CLASS_LENGTH = 32;

    /**
     * Уникальный номер реактива. Программа назначает сама
     */
    private long id;
    /**
     * Название реактива (например "Sodium chloride"). Нельзя пустое. До 128 символов.
     */
    private String name;
    /**
     * Формула (например "NaCl"). Можно пусто, но если есть — до 32 символов.
     */
    private String formula;
    /**
     * CAS номер (например "7647-14-5"). Можно пусто. До 32 символов.
     */
    private String cas;
    /**
     * Класс опасности (например "low", "flammable", "toxic"). Можно пусто. До 32 символов.
     */
    private String hazardClass;
    /**
     * Кто создал запись (логин).
     */
    private String ownerUsername;
    /**
     * Когда создано. Программа ставит автоматически.
     */
    private Instant createdAt;
    /**
     * Когда обновляли. Программа обновляет автоматически
     */
    private Instant updatedAt;

    /**
     * Конструктор по умолчанию.
     */
    public Reagent() {
    }

    /**
     * Конструктор со всеми полями.
     */
    public Reagent(long id, String name, String formula, String cas, String hazardClass,
                   String ownerUsername, Instant createdAt, Instant updatedAt) {
        this.id = id;
        setName(name);
        setFormula(formula);
        setCas(cas);
        setHazardClass(hazardClass);
        this.ownerUsername = ownerUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //TODO прописать очев javadoc
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("название не может быть пустым");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("название слишком длинное (макс. " + MAX_NAME_LENGTH + ")");
        }
        this.name = name.trim();
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        if (formula != null && formula.length() > MAX_FORMULA_LENGTH) {
            throw new IllegalArgumentException("формула слишком длинная (макс. " + MAX_FORMULA_LENGTH + ")");
        }
        this.formula = formula != null ? formula.trim() : null;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        if (cas != null && cas.length() > MAX_CAS_LENGTH) {
            throw new IllegalArgumentException("CAS слишком длинный (макс. " + MAX_CAS_LENGTH + ")");
        }
        this.cas = cas != null ? cas.trim() : null;
    }

    public String getHazardClass() {
        return hazardClass;
    }

    public void setHazardClass(String hazardClass) {
        if (hazardClass != null && hazardClass.length() > MAX_HAZARD_CLASS_LENGTH) {
            throw new IllegalArgumentException("класс опасности слишком длинный (макс. " + MAX_HAZARD_CLASS_LENGTH + ")");
        }
        this.hazardClass = hazardClass != null ? hazardClass.trim() : null;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reagent reagent = (Reagent) o;
        return id == reagent.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reagent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", cas='" + cas + '\'' +
                ", hazardClass='" + hazardClass + '\'' +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}