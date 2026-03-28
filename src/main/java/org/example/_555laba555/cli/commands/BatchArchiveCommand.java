package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.BatchStatus;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

public class BatchArchiveCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: batch_archive <ID>");
            return;
        }

        try {
            long id = Long.parseLong(args.trim());
            ReagentBatch batch = services.getBatchService().getById(id);

            if (batch == null) {
                System.out.println("Партия не найдена");
                return;
            }

            if (batch.getStatus() == BatchStatus.ARCHIVED) {
                System.out.println("Партия уже в архиве");
                return;
            }

            services.getBatchService().archive(id);
            System.out.println("Партия архивирована");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        }
    }
}