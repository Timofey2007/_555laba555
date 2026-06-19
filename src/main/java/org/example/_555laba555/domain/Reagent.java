package org.example._555laba555.domain;

import java.time.Instant;
import java.util.Objects;

public final class Reagent {
    public static final int MAX_NAME_LENGTH = 128;
    public static final int MAX_FORMULA_LENGTH = 32;
    public static final int MAX_CAS_LENGTH = 32;
    public static final int MAX_HAZARD_CLASS_LENGTH = 32;

    private long id;
    private String name;
    private String formula;
    private String cas;
    private String hazardClass;
    private String ownerName;
    private Instant createdAt;
    private Instant updatedAt;
    private long ownerId;

    public Reagent() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getOwnerId() { return ownerId; }
    public void setOwnerId(long ownerId) { this.ownerId = ownerId; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("название не может быть пустым");
        if (name.length() > MAX_NAME_LENGTH) throw new IllegalArgumentException("название слишком длинное");
        this.name = name.trim();
    }

    public String getFormula() { return formula; }
    public void setFormula(String formula) {
        if (formula != null && formula.length() > MAX_FORMULA_LENGTH) throw new IllegalArgumentException("формула слишком длинная");
        this.formula = formula != null ? formula.trim() : null;
    }

    public String getCas() { return cas; }
    public void setCas(String cas) {
        if (cas != null && cas.length() > MAX_CAS_LENGTH) throw new IllegalArgumentException("CAS слишком длинный");
        this.cas = cas != null ? cas.trim() : null;
    }

    public String getHazardClass() { return hazardClass; }
    public void setHazardClass(String hazardClass) {
        if (hazardClass != null && hazardClass.length() > MAX_HAZARD_CLASS_LENGTH) throw new IllegalArgumentException("класс опасности слишком длинный");
        this.hazardClass = hazardClass != null ? hazardClass.trim() : null;
    }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reagent reagent = (Reagent) o;
        return id == reagent.id;
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