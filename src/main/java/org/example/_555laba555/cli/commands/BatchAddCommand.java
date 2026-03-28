package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.*;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class BatchAddCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        if (services.getReagentService().isEmpty()) {
            System.out.println("Сначала добавьте реактив");
            return;
        }

        long reagentId = input.readLong("ID реактива: ");
        if (!services.getReagentService().exist(reagentId)) {
            System.out.println("Реактив не найден");
            return;
        }

        ReagentBatch b = new ReagentBatch();
        b.setReagentId(reagentId);
        b.setLabel(input.readString("Метка (номер партии): ", true));
        b.setQuantityCurrent(input.readDouble("Количество: "));

        String unitStr = input.readString("Единицы (G или ML): ", true).toUpperCase();
        b.setUnit(BatchUnit.valueOf(unitStr));
        b.setLocation(input.readString("Местоположение: ", true));

        LocalDate expires = input.readDate("Срок годности (ГГГГ-ММ-ДД, пусто если нет): ");
        if (expires != null) {
            b.setExpiresAt(expires.atStartOfDay(ZoneOffset.UTC).toInstant());
        }

        int statusChoice;
        while (true) {
            statusChoice = input.readInt("Статус (1-ACTIVE, 2-ARCHIVED): ");
            if (statusChoice == 1 || statusChoice == 2) break;
            System.out.println("Ошибка: введите 1 или 2");
        }
        b.setStatus(statusChoice == 1 ? BatchStatus.ACTIVE : BatchStatus.ARCHIVED);
        b.setOwnerUsername(input.readString("Владелец: ", true));

        services.getBatchService().add(b);
        System.out.println("Партия добавлена. ID: " + b.getId());
    }
}