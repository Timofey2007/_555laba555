package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.*;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

public class MoveAddCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: move_add <ID>");
            return;
        }

        long batchId;
        try {
            batchId = Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            return;
        }

        ReagentBatch batch = services.getBatchService().getById(batchId);
        if (batch == null) {
            System.out.println("Партия не найдена");
            return;
        }

        if (batch.getStatus() == BatchStatus.ARCHIVED) {
            System.out.println("Нельзя добавлять движения к архивной партии");
            return;
        }

        System.out.println("Текущий остаток: " + batch.getQuantityCurrent() + " " + batch.getUnit());

        StockMove move = new StockMove();
        move.setBatchId(batchId);
        move.setUnit(batch.getUnit());

        int typeChoice;
        while (true) {
            typeChoice = input.readInt("Тип (1-IN, 2-OUT, 3-DISCARD): ");
            if (typeChoice >= 1 && typeChoice <= 3) break;
            System.out.println("Ошибка: введите число от 1 до 3");
        }

        switch (typeChoice) {
            case 1: move.setType(StockMoveType.IN); break;
            case 2: move.setType(StockMoveType.OUT); break;
            case 3: move.setType(StockMoveType.DISCARD); break;
        }

        double qty = input.readDouble("Количество: ");
        move.setQuantity(qty);

        String reason = input.readOptional("Причина (пусто если нет): ");
        if (!reason.isEmpty()) move.setReason(reason);

        String owner = input.readString("Владелец (Enter - system): ", false);
        move.setOwnerUsername(owner.isEmpty() ? "system" : owner);

        services.getMoveService().add(move, batch.getQuantityCurrent());

        if (move.getType() == StockMoveType.IN) {
            batch.setQuantityCurrent(batch.getQuantityCurrent() + qty);
        } else {
            batch.setQuantityCurrent(batch.getQuantityCurrent() - qty);
        }

        services.getBatchService().update(batch);

        System.out.println("Движение добавлено. Новый остаток: " +
                batch.getQuantityCurrent() + " " + batch.getUnit());
    }
}