package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.StockMove;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

public class MoveDelete implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) {
        if (args == null || args.trim().isEmpty()) return;
        long moveId;
        try {
            moveId = Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            return;
        }

        StockMove move = services.getMoveService().getMoveById(moveId);
        if (move == null) {
            System.out.println("Движение не найдено");
            return;
        }

        System.out.println("Вы собираетесь удалить движение ID: " + move.getId());
        System.out.print("Уверены? (Y/N): ");
        try {
            String confirm = input.readString("", true);
            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("Удаление отменено");
                return;
            }
        } catch (Exception e) {
            System.out.println("Ошибка ввода");
            return;
        }

        CommandHistory history = new CommandHistory("delete", "move");
        history.setDeletedObject(copyMove(move));
        history.setObjectId(moveId);
        services.pushHistory(history);

        services.getMoveService().remove(moveId);
        System.out.println("Движение удалено");
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