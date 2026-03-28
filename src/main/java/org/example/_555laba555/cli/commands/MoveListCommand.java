package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.domain.*;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

import java.util.List;

public class MoveListCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: move_list <ID> [--last N]");
            return;
        }

        String[] parts = args.trim().split("\\s+");
        long batchId;
        try {
            batchId = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            return;
        }

        ReagentBatch batch = services.getBatchService().getById(batchId);
        if (batch == null) {
            System.out.println("Партия не найдена");
            return;
        }

        int limit = Integer.MAX_VALUE;
        for (int i = 1; i < parts.length; i++) {
            if ("--last".equals(parts[i]) && i + 1 < parts.length) {
                try {
                    limit = Integer.parseInt(parts[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: после --last должно быть число");
                    return;
                }
                break;
            }
        }

        List<StockMove> moves = services.getMoveService().getByBatchId(batchId, limit);

        if (moves.isEmpty()) {
            System.out.println("Движений нет");
            return;
        }

        System.out.println("\nДВИЖЕНИЯ ДЛЯ ПАРТИИ " + batchId);
        System.out.println("Партия: " + batch.getLabel());
        System.out.println("Текущий остаток: " + batch.getQuantityCurrent() + " " + batch.getUnit());
        System.out.println();
        System.out.printf("%-5s %-20s %-8s %-10s %s%n",
                "ID", "Дата", "Тип", "Количество", "Причина");

        for (StockMove m : moves) {
            String date = m.getMovedAt() != null ?
                    m.getMovedAt().toString().substring(0, 19).replace("T", " ") : "не указано";
            String sign = m.getType() == StockMoveType.IN ? "+" : "-";
            System.out.printf("%-5d %-20s %-8s %s%-9s %s%n",
                    m.getId(), date, m.getType(), sign,
                    m.getQuantity() + " " + m.getUnit(),
                    m.getReason() != null ? m.getReason() : "");
        }
    }
}