package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.*;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class BatchAddCommand implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) throws Exception {
        if (!services.getUserService().isAuthenticated()) {
            System.out.println("Ошибка: необходимо авторизоваться");
            return;
        }
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
        b.setLabel(input.readString("Метка: ", true));
        b.setQuantityCurrent(input.readDouble("Количество: "));
        b.setUnit(BatchUnit.valueOf(input.readString("Единицы (G или ML): ", true).toUpperCase()));
        b.setLocation(input.readString("Местоположение: ", true));

        LocalDate expires = input.readDate("Срок годности (ГГГГ-ММ-ДД): ");
        if (expires != null) b.setExpiresAt(expires.atStartOfDay(ZoneOffset.UTC).toInstant());

        int statusChoice = input.readInt("Статус (1-ACTIVE, 2-ARCHIVED): ");
        b.setStatus(statusChoice == 1 ? BatchStatus.ACTIVE : BatchStatus.ARCHIVED);

        b.setOwnerID(services.getUserService().getCurrentUserId());
        b.setOwnerUsername(services.getUserService().getCurrentUserLogin());

        services.getBatchService().add(b);
        System.out.println("Партия добавлена. ID: " + b.getId());
    }
}