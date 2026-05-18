package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.*;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class StockReportCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        LocalDate expiresBefore = null;
        if (args.contains("--expires-before")) {
            String[] parts = args.split("--expires-before");
            if (parts.length > 1) {
                try {
                    expiresBefore = LocalDate.parse(parts[1].trim());
                } catch (Exception e) {
                    System.out.println("Ошибка: неверный формат даты");
                    return;
                }
            }
        }

        List<ReagentBatch> batches = services.getBatchService().getAll();
        List<Reagent> reagents = services.getReagentService().getAll();

        long active = batches.stream().filter(b -> b.getStatus() == BatchStatus.ACTIVE).count();
        long archived = batches.size() - active;

        System.out.println("\nОТЧЕТ ПО СКЛАДУ");
        System.out.println("Дата: " + LocalDate.now());
        System.out.println("Всего партий: " + batches.size() +
                " (ACTIVE: " + active + ", ARCHIVED: " + archived + ")");
        System.out.println("Всего реактивов: " + reagents.size());

        if (expiresBefore != null) {
            System.out.println("\nПАРТИИ С ИСТЕКАЮЩИМ СРОКОМ (до " + expiresBefore + "):");
            Instant before = expiresBefore.atStartOfDay(ZoneOffset.UTC).toInstant();
            LocalDate now = LocalDate.now();

            for (ReagentBatch b : batches) {
                if (b.getStatus() != BatchStatus.ACTIVE || b.getExpiresAt() == null) continue;
                if (!b.getExpiresAt().isBefore(before)) continue;

                LocalDate expDate = b.getExpiresAt().atZone(ZoneOffset.UTC).toLocalDate();
                long days = java.time.temporal.ChronoUnit.DAYS.between(now, expDate);
                String warn = days < 30 ? " (СКОРО)" : "";

                System.out.printf("ID: %d | %s | %.1f %s | Годен до: %s%s%n",
                        b.getId(), b.getLabel(), b.getQuantityCurrent(), b.getUnit(), expDate, warn);
            }
        }
    }
}