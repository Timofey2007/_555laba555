package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

import java.util.List;

public class BatchListCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        List<ReagentBatch> batches;

        if (args.trim().isEmpty()) {
            batches = services.getBatchService().getAll();
            System.out.println("ВСЕ ПАРТИИ:");
        } else {
            try {
                long reagentId = Long.parseLong(args.trim());
                batches = services.getBatchService().getByReagentId(reagentId);
                System.out.println("ПАРТИИ ДЛЯ РЕАКТИВА " + reagentId + ":");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: ID должен быть числом");
                return;
            }
        }

        if (batches.isEmpty()) {
            System.out.println("Партии не найдены");
            return;
        }

        for (ReagentBatch b : batches) {
            String expires = b.getExpiresAt() != null ?
                    b.getExpiresAt().toString().substring(0, 10) : "не указан";
            System.out.printf("ID: %d | %s | %.1f %s | %s | %s | %s%n",
                    b.getId(), b.getLabel(), b.getQuantityCurrent(), b.getUnit(),
                    b.getLocation(), expires, b.getStatus());
        }
    }
}