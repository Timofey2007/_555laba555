package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.domain.StockMove;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

import java.io.IOException;

public class MoveDelete implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args){
        if (args == null || args.trim().isEmpty()) return;
        long moveId;
        try {
            moveId = Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        StockMove reagent = services.getMoveService().getMoveById(moveId);
        if (reagent == null) return;
        System.out.println("Вы собираетесь удалить реагент" + reagent + "Уверены что вы хотите удалить реагент? Y/N");
        try{
            String confirm = input.readString("", true);
            if (!confirm.equalsIgnoreCase("Y")) return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CommandHistory history = new CommandHistory("delete", "move");
        history.setDeletedObject(copyMove(reagent));
        history.setObjectId(moveId);
        services.pushHistory(history);

        services.getReagentService().remove(moveId);

    }
    private StockMove copyMove(StockMove original) {
        StockMove copy = new StockMove();
        copy.setId(original.getId());
        copy.setBatchId(original.getBatchId());
        copy.setType(original.getType());
        copy.setQuantity(original.getQuantity());
        copy.setUnit(original.getUnit());
        copy.setReason(original.getReason());
        copy.setOwnerUsername(original.getOwnerUsername());
        copy.setMovedAt(original.getMovedAt());
        copy.setCreatedAt(original.getCreatedAt());
        return copy;
    }
}

