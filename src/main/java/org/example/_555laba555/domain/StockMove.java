package org.example._555laba555.domain;

import java.time.Instant;
import java.util.Objects;

/**
 * Движение по бутылке (приход, расход, списание).
 */
public final class StockMove {
    public static final int MAX_REASON_LENGTH = 128;

    private long id;
    private long batchId;
    private StockMoveType type;
    private double quantity;
    private BatchUnit unit;
    private String reason;
    private String ownerUsername;
    private Instant movedAt;
    private Instant createdAt;

    // НОВЫЕ ПОЛЯ ДЛЯ ВЛАДЕЛЬЦА
    private long ownerId;
    private String ownerName;

    public StockMove() {
    }

    public StockMove(long id, long batchId, StockMoveType type, double quantity, BatchUnit unit,
                     String reason, String ownerUsername, Instant movedAt, Instant createdAt) {
        this.id = id;
        this.batchId = batchId;
        setType(type);
        setQuantity(quantity);
        setUnit(unit);
        setReason(reason);
        this.ownerUsername = ownerUsername;
        this.movedAt = movedAt;
        this.createdAt = createdAt;
    }

    // СУЩЕСТВУЮЩИЕ ГЕТТЕРЫ/СЕТТЕРЫ
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getBatchId() { return batchId; }
    public void setBatchId(long batchId) { this.batchId = batchId; }

    public StockMoveType getType() { return type; }
    public void setType(StockMoveType type) {
        if (type == null) {
            throw new IllegalArgumentException("тип движения должен быть указан");
        }
        this.type = type;
    }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("количество должно быть положительным");
        }
        this.quantity = quantity;
    }

    public BatchUnit getUnit() { return unit; }
    public void setUnit(BatchUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("единицы измерения должны быть указаны");
        }
        this.unit = unit;
    }

    public String getReason() { return reason; }
    public void setReason(String reason) {
        if (reason != null && reason.length() > MAX_REASON_LENGTH) {
            throw new IllegalArgumentException("причина слишком длинная (макс. " + MAX_REASON_LENGTH + ")");
        }
        this.reason = reason != null ? reason.trim() : null;
    }

    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

    public Instant getMovedAt() { return movedAt; }
    public void setMovedAt(Instant movedAt) { this.movedAt = movedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    // НОВЫЕ ГЕТТЕРЫ/СЕТТЕРЫ ДЛЯ ВЛАДЕЛЬЦА
    public long getOwnerId() { return ownerId; }
    public void setOwnerId(long ownerId) { this.ownerId = ownerId; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockMove stockMove = (StockMove) o;
        return id == stockMove.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StockMove{" +
                "id=" + id +
                ", batchId=" + batchId +
                ", type=" + type +
                ", quantity=" + quantity +
                ", unit=" + unit +
                ", reason='" + reason + '\'' +
                ", ownerUsername='" + ownerUsername + '\'' +
                ", movedAt=" + movedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}