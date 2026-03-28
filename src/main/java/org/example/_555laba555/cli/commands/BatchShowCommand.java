package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.*;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

import java.util.List;

public class BatchShowCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: batch_show <ID>");
            return;
        }

        try {
            long id = Long.parseLong(args.trim());
            ReagentBatch b = services.getBatchService().getById(id);

            if (b == null) {
                System.out.println("Партия не найдена");
                return;
            }

            System.out.println("\n=== ПАРТИЯ ID: " + id + " ===");
            System.out.println("Метка: " + b.getLabel());
            System.out.println("ID реактива: " + b.getReagentId());
            System.out.println("Количество: " + b.getQuantityCurrent() + " " + b.getUnit());
            System.out.println("Место: " + b.getLocation());
            System.out.println("Срок годности: " + (b.getExpiresAt() != null ? b.getExpiresAt() : "не указан"));
            System.out.println("Статус: " + b.getStatus());
            System.out.println("Владелец: " + b.getOwnerUsername());

            List<StockMove> moves = services.getMoveService().getByBatchId(id, 5);
            if (!moves.isEmpty()) {
                System.out.println("\nПоследние движения:");
                for (StockMove m : moves) {
                    String sign = m.getType() == StockMoveType.IN ? "+" : "-";
                    System.out.printf("  %s %s%.1f %s %s%n",
                            m.getType(), sign, m.getQuantity(), m.getUnit(),
                            m.getReason() != null ? "(" + m.getReason() + ")" : "");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        }
    }
}