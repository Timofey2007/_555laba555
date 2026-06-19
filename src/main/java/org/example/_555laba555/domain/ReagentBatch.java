package org.example._555laba555.domain;

import java.time.Instant;
import java.util.Objects;

public final class ReagentBatch {
    public static final int MAX_LABEL_LENGTH = 64;
    public static final int MAX_LOCATION_LENGTH = 64;

    private long id;
    private long reagentId;
    private String label;
    private double quantityCurrent;
    private BatchUnit unit;
    private String location;
    private Instant expiresAt;
    private BatchStatus status;
    private String ownerUsername;
    private Instant createdAt;
    private Instant updatedAt;
    private long ownerId;

    public ReagentBatch() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getReagentId() { return reagentId; }
    public void setReagentId(long reagentId) { this.reagentId = reagentId; }

    public String getLabel() { return label; }
    public void setLabel(String label) {
        if (label == null || label.trim().isEmpty()) throw new IllegalArgumentException("метка партии не может быть пустой");
        if (label.length() > MAX_LABEL_LENGTH) throw new IllegalArgumentException("метка слишком длинная");
        this.label = label.trim();
    }

    public double getQuantityCurrent() { return quantityCurrent; }
    public void setQuantityCurrent(double quantityCurrent) {
        if (quantityCurrent < 0) throw new IllegalArgumentException("текущее количество не может быть отрицательным");
        this.quantityCurrent = quantityCurrent;
    }

    public BatchUnit getUnit() { return unit; }
    public void setUnit(BatchUnit unit) {
        if (unit == null) throw new IllegalArgumentException("единицы измерения должны быть указаны");
        this.unit = unit;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) throw new IllegalArgumentException("местоположение не может быть пустым");
        if (location.length() > MAX_LOCATION_LENGTH) throw new IllegalArgumentException("местоположение слишком длинное");
        this.location = location.trim();
    }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public BatchStatus getStatus() { return status; }
    public void setStatus(BatchStatus status) { this.status = status; }

    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

    public long getOwnerId() { return ownerId; }
    public void setOwnerId(long ownerId) { this.ownerId = ownerId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReagentBatch that = (ReagentBatch) o;
        return id == that.id;
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