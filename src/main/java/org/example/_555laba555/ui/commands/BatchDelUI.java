package org.example._555laba555.ui.commands;

import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.StockMove;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

import java.util.Scanner;

public class BatchDelUI {
    private final ServiceManager services;

    public BatchDelUI(ServiceManager services){
        this.services = services;
    }
    public void execute(long batchId){
        Long login = services.getUserService().getCurrentUser().getId();
        ReagentBatch Nashmove = services.getBatchService().getById(batchId);
        long nashid = Nashmove.getOwnerID();
        if(nashid == login){
            ReagentBatch batch = services.getBatchService().getById(batchId);
            CommandHistory history = new CommandHistory("delete", "batch");
            history.setDeletedObject(copyBatch(batch));
            history.setObjectId(batchId);
            services.pushHistory(history);

            services.getBatchService().remove(batchId);
            }else {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Доступ запрещен");
            alert.setContentText("Вы не являетесь владельцем этой партии и не можете его удалить.");
            alert.showAndWait();
        }
    }

    private ReagentBatch copyBatch(ReagentBatch original) {
        ReagentBatch copy = new ReagentBatch();
        copy.setId(original.getId());
        copy.setReagentId(original.getReagentId());
        copy.setLabel(original.getLabel());
        copy.setQuantityCurrent(original.getQuantityCurrent());
        copy.setUnit(original.getUnit());
        copy.setLocation(original.getLocation());
        copy.setExpiresAt(original.getExpiresAt());
        copy.setStatus(original.getStatus());
        copy.setOwnerID(original.getOwnerID());
        copy.setOwnerUsername(original.getOwnerUsername());
        copy.setCreatedAt(original.getCreatedAt());
        copy.setUpdatedAt(original.getUpdatedAt());
        return copy;
    }
}