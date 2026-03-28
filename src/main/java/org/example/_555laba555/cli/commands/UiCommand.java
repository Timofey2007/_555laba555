package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.ui.ReagentUI;
import org.example._555laba555.fileManager.StorageException;

public class UiCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        System.out.println("Запуск графического интерфейса...");
        System.out.println("Консольный режим будет закрыт.");

        try {
            storage.save(services);
            System.out.println("Данные сохранены");
        } catch (StorageException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }

        ReagentUI.setServicesAndStorage(services, storage);

        new Thread(() -> {
            try {
                ReagentUI.main(new String[0]);
            } catch (Exception e) {
                System.out.println("Ошибка запуска интерфейса: " + e.getMessage());
            }
        }).start();
    }
}