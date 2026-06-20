package org.example._555laba555.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryRecord {
    private long id;
    private String objectType;
    private long objectId;
    private String objectData;
    private LocalDateTime deletedAt;
    private long deletedBy;
    private String deletedByName;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getObjectType() { return objectType; }
    public void setObjectType(String objectType) { this.objectType = objectType; }

    public long getObjectId() { return objectId; }
    public void setObjectId(long objectId) { this.objectId = objectId; }

    public String getObjectData() { return objectData; }
    public void setObjectData(String objectData) { this.objectData = objectData; }

    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public long getDeletedBy() { return deletedBy; }
    public void setDeletedBy(long deletedBy) { this.deletedBy = deletedBy; }

    public void setDeletedByName(String deletedByName) { this.deletedByName = deletedByName; }


}