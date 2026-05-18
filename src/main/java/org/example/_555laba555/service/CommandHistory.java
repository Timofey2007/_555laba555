package org.example._555laba555.service;

import java.time.LocalDateTime;

public class CommandHistory {
    private final String action;
    private final String typeOfDeleted;
    private final LocalDateTime whenWasDeleted;
    private Object deletedObject;
    private Long objectId;

    public CommandHistory(String action, String typeOfDeleted){
        this.action = action;
        this.typeOfDeleted = typeOfDeleted;
        this.whenWasDeleted = LocalDateTime.now();
    }
    public String getTypeOfDeleted(){
        return typeOfDeleted;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s %s", whenWasDeleted, action, typeOfDeleted, objectId);
    }
    public void setDeletedObject(Object deletedObject) {
        this.deletedObject = deletedObject;
    }
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Object getDeletedObject() {
        return deletedObject;
    }

}