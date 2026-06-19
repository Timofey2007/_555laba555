package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

import java.util.Scanner;

public class BatchDelete implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) {
        if (args == null || args.trim().isEmpty()) return;
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
        
        System.out.println("Вы собираетесь удалить партию " + batch.getId() + ": " + batch);
        System.out.print("Уверены, что хотите удалить эту партию? Y/N: ");

        try (Scanner scanner = new Scanner(System.in)) {
            String consInput = scanner.nextLine().trim();
            if (!consInput.equalsIgnoreCase("Y")) {
                System.out.println("Вы отменили удаление");
                return;
            }
        } catch (Exception e) {
            System.out.println("Ошибка ввода");
            return;
        }

        CommandHistory history = new CommandHistory("delete", "batch");
        history.setDeletedObject(copyBatch(batch));
        history.setObjectId(batchId);
        services.pushHistory(history);

        services.getBatchService().remove(batchId);
        System.out.println("Партия удалена");
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