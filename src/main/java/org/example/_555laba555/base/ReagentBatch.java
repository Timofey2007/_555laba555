package org.example._555laba555.base;

import java.time.Instant;
import java.util.Objects;

/**
 * Партия/бутылка реактива.
 */
public final class ReagentBatch {
    public static final int MAX_LABEL_LENGTH = 64;
    public static final int MAX_LOCATION_LENGTH = 64;

    /**
     *Уникальный номер бутылки/партии. Программа назначает сама
     */
    private long id;
    /**
     *  К какому реактиву относится (id реактива).
     *  Должен ссылаться на реально существующий Reagent.
     */
    private long reagentId;
    /**
     * Человеческая метка/номер партии (например "NaCl-2026-01"). Нельзя пустое. До 64 символов.
     */
    private String label;
    /**
     * Текущий остаток (число). Не должен быть отрицательным.
     */
    private double quantityCurrent;
    /**
     * Единицы для quantity (g или mL).
     */
    private BatchUnit unit;
    /**
     * Где хранится (например "Shelf-1"). Нельзя пустое. До 64 символов.
     */
    private String location;
    /**
     * Срок годности. Можно null (если не указан).
     */
    private Instant expiresAt;
    /**
     * Статус: ACTIVE или ARCHIVED. ARCHIVED = “в работе больше не используем”.
     */
    private BatchStatus status;
    /**
     * Кто создал (логин).
     */
    private String ownerUsername;
    /**
     * Когда создано. Программа ставит автоматически.
     */
    private Instant createdAt;
    /**
     * Когда обновляли. Программа обновляет автоматически.
     */
    private Instant updatedAt;

    public ReagentBatch() {
    }

    public ReagentBatch(long id, long reagentId, String label, double quantityCurrent, BatchUnit unit,
                        String location, Instant expiresAt, BatchStatus status, String ownerUsername,
                        Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.reagentId = reagentId;
        setLabel(label);
        setQuantityCurrent(quantityCurrent);
        setUnit(unit);
        setLocation(location);
        this.expiresAt = expiresAt;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReagentId() {
        return reagentId;
    }

    public void setReagentId(long reagentId) {
        this.reagentId = reagentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new IllegalArgumentException("метка партии не может быть пустой");
        }
        if (label.length() > MAX_LABEL_LENGTH) {
            throw new IllegalArgumentException("метка слишком длинная (макс. " + MAX_LABEL_LENGTH + ")");
        }
        this.label = label.trim();
    }

    public double getQuantityCurrent() {
        return quantityCurrent;
    }

    public void setQuantityCurrent(double quantityCurrent) {
        if (quantityCurrent < 0) {
            throw new IllegalArgumentException("текущее количество не может быть отрицательным");
        }
        this.quantityCurrent = quantityCurrent;
    }

    public BatchUnit getUnit() {
        return unit;
    }

    public void setUnit(BatchUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("единицы измерения должны быть указаны");
        }
        this.unit = unit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("местоположение не может быть пустым");
        }
        if (location.length() > MAX_LOCATION_LENGTH) {
            throw new IllegalArgumentException("местоположение слишком длинное (макс. " + MAX_LOCATION_LENGTH + ")");
        }
        this.location = location.trim();
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public void setStatus(BatchStatus status) {
        this.status = status;
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
        ReagentBatch that = (ReagentBatch) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReagentBatch{" +
                "id=" + id +
                ", reagentId=" + reagentId +
                ", label='" + label + '\'' +
                ", quantityCurrent=" + quantityCurrent +
                ", unit=" + unit +
                ", location='" + location + '\'' +
                ", expiresAt=" + expiresAt +
                ", status=" + status +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}