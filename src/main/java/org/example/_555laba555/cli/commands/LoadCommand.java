package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.fileManager.StorageException;

public class LoadCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        try {
            if (args.trim().isEmpty()) {
                storage.load(services);
                System.out.println("Данные загружены");
            } else {
                Conservation customStorage = new Conservation(args.trim());
                customStorage.load(services);
                System.out.println("Данные загружены из: " + args);
            }
        } catch (StorageException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }
    }
}
