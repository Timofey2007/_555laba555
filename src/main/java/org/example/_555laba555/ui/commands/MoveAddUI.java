package org.example._555laba555.ui.commands;

import org.example._555laba555.domain.BatchUnit;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.StockMove;
import org.example._555laba555.domain.StockMoveType;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.validation.ValidationException;

public class MoveAddUI {
    private final ServiceManager services;

    public MoveAddUI(ServiceManager services) {
        this.services = services;
    }

    public void execute(long batchId, StockMoveType type, double quantity, BatchUnit unit,
                        String reason) {

        ReagentBatch batch = services.getBatchService().getById(batchId);
        if (batch == null) {
            throw new ValidationException("Партия с ID " + batchId + " не найдена");
        }
        String login = services.getUserService().getCurrentUserLogin();

        StockMove move = new StockMove();
        move.setBatchId(batchId);
        move.setType(type);
        move.setQuantity(quantity);
        move.setUnit(unit);
        move.setReason(reason);
        move.setOwnerUsername(login);
        if (move.getType() == StockMoveType.IN) {
            batch.setQuantityCurrent(batch.getQuantityCurrent() + quantity);
        } else {
            batch.setQuantityCurrent(batch.getQuantityCurrent() - quantity);
        }

        services.getMoveService().add(move, batch.getQuantityCurrent());
    }
}