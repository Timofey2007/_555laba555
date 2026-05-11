package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

import java.io.IOException;

public class ReagentDelete implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) {
        if (args == null || args.trim().isEmpty()) return;
        long reagentId;
        try {
            reagentId = Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        Reagent reagent = services.getReagentService().getReagentById(reagentId);
        if (reagent == null) return;
        System.out.println("Вы собираетесь удалить реагент" + reagent + "Уверены что вы хотите удалить реагент? Y/N");
        try{
            System.out.println("Уверены что хотите удалить? Y/N");
            String confirm = input.readString("", true);
            if (!confirm.equalsIgnoreCase("Y")) return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CommandHistory history = new CommandHistory("delete", "reagent");
        history.setDeletedObject(copyReagent(reagent));
        history.setObjectId(reagentId);
        services.pushHistory(history);

        services.getReagentService().remove(reagentId);

    }

    private Reagent copyReagent(Reagent original) {
        Reagent copy = new Reagent();
        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setFormula(original.getFormula());
        copy.setCas(original.getCas());
        copy.setHazardClass(original.getHazardClass());
        copy.setOwnerUsername(original.getOwnerUsername());
        copy.setCreatedAt(original.getCreatedAt());
        copy.setUpdatedAt(original.getUpdatedAt());
        return copy;
    }
}
