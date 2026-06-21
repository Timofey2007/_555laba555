package org.example._555laba555.ui.commands;

import org.example._555laba555.domain.BatchUnit;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.service.ServiceManager;

import java.time.Instant;
import java.time.LocalDate;

public class BatchEdUI {
    private final ServiceManager services;
    public BatchEdUI(ServiceManager services){
        this.services = services;
    }
    public void execute(long batchid, long reagid, String label, Double quantity, BatchUnit unit, String location, Instant expiresAt){
        Long login = services.getUserService().getCurrentUser().getId();
        ReagentBatch Nashbatch = services.getBatchService().getById(batchid);
        long nashid = Nashbatch.getOwnerID();
        if(nashid == login){
            Nashbatch.setId(batchid);
            Nashbatch.setReagentId(reagid);
            Nashbatch.setLabel(label);
            Nashbatch.setQuantityCurrent(quantity);
            Nashbatch.setUnit(unit);
            Nashbatch.setLocation(location);
            Nashbatch.setExpiresAt(expiresAt);

            services.getBatchService().update(Nashbatch);

        }else{
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Доступ запрещен");
            alert.setContentText("Вы не являетесь владельцем этой партии и не можете его удалить.");
            alert.showAndWait();
        }
    }
}
