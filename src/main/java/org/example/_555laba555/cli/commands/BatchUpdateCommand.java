package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.*;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class BatchUpdateCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: batch_update <ID> поле=значение ...");
            System.out.println("Доступные поля: location, label, status, expiresAt");
            return;
        }

        String[] parts = args.trim().split("\\s+");
        long id;
        try {
            id = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            return;
        }

        ReagentBatch batch = services.getBatchService().getById(id);
        if (batch == null) {
            System.out.println("Партия не найдена");
            return;
        }

        boolean updated = false;

        for (int i = 1; i < parts.length; i++) {
            String[] kv = parts[i].split("=", 2);
            if (kv.length != 2) {
                System.out.println("Пропущено: " + parts[i] + " - нужен формат поле=значение");
                continue;
            }

            String field = kv[0].toLowerCase();
            String value = kv[1];

            try {
                switch (field) {
                    case "location":
                        batch.setLocation(value);
                        updated = true;
                        break;
                    case "label":
                        batch.setLabel(value);
                        updated = true;
                        break;
                    case "status":
                        batch.setStatus(BatchStatus.valueOf(value.toUpperCase()));
                        updated = true;
                        break;
                    case "expiresat":
                        LocalDate date = LocalDate.parse(value);
                        batch.setExpiresAt(date.atStartOfDay(ZoneOffset.UTC).toInstant());
                        updated = true;
                        break;
                    default:
                        System.out.println("Неизвестное поле: " + field);
                }
            } catch (Exception e) {
                System.out.println("Ошибка в поле " + field + ": " + e.getMessage());
            }
        }

        if (updated) {
            services.getBatchService().update(batch);
            System.out.println("Партия обновлена");
        }
    }
}