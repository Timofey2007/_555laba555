package org.example._555laba555.ui.commands;

import org.example._555laba555.domain.StockMove;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

public class MoveDelUI {
    private final ServiceManager services;

    public MoveDelUI(ServiceManager services){
        this.services = services;
    }
    public void execute(long moveId){
        Long login = services.getUserService().getCurrentUser().getId();
        StockMove Nashmove = services.getMoveService().getMoveById(moveId);
        long nashid = Nashmove.getOwnerID();
        if(nashid == login){
        StockMove move = services.getMoveService().getMoveById(moveId);
        CommandHistory history = new CommandHistory("delete", "move");
        history.setDeletedObject(copyMove(move));
        history.setObjectId(moveId);
        services.pushHistory(history);
        services.getMoveService().remove(moveId);}else{
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Доступ запрещен");
            alert.setContentText("Вы не являетесь владельцем этого перемещения и не можете его удалить.");
            alert.showAndWait();
        }
    }

    private StockMove copyMove(StockMove original) {
        StockMove copy = new StockMove();
        copy.setId(original.getId());
        copy.setBatchId(original.getBatchId());
        copy.setType(original.getType());
        copy.setQuantity(original.getQuantity());
        copy.setUnit(original.getUnit());
        copy.setReason(original.getReason());
        copy.setOwnerID(original.getOwnerID());
        copy.setOwnerUsername(original.getOwnerUsername());
        copy.setMovedAt(original.getMovedAt());
        copy.setCreatedAt(original.getCreatedAt());
        return copy;
    }
}

