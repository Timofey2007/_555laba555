package org.example._555laba555.domain;

import java.time.Instant;
import java.util.Objects;

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
    private long ownerId;

    public StockMove() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getBatchId() { return batchId; }
    public void setBatchId(long batchId) { this.batchId = batchId; }

    public StockMoveType getType() { return type; }
    public void setType(StockMoveType type) {
        if (type == null) throw new IllegalArgumentException("тип движения должен быть указан");
        this.type = type;
    }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("количество должно быть положительным");
        this.quantity = quantity;
    }

    public BatchUnit getUnit() { return unit; }
    public void setUnit(BatchUnit unit) {
        if (unit == null) throw new IllegalArgumentException("единицы измерения должны быть указаны");
        this.unit = unit;
    }

    public String getReason() { return reason; }
    public void setReason(String reason) {
        if (reason != null && reason.length() > MAX_REASON_LENGTH) throw new IllegalArgumentException("причина слишком длинная");
        this.reason = reason != null ? reason.trim() : null;
    }

    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

    public long getOwnerId() { return ownerId; }
    public void setOwnerId(long ownerId) { this.ownerId = ownerId; }

    public Instant getMovedAt() { return movedAt; }
    public void setMovedAt(Instant movedAt) { this.movedAt = movedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockMove stockMove = (StockMove) o;
        return id == stockMove.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
    public void setOwnerID(long ownerID){
        this.ownerId = ownerID;
    }
    public long getOwnerID(){
        return ownerId;
    }

}