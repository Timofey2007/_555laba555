package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.fileManager.StorageException;

public class LoadCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws StorageException  {
        String wayToPath = args.trim().isEmpty() ? storage.getCsvFile() : args.trim(); //загрузили отсюда
        Conservation needed = new Conservation(wayToPath);
        needed.load(services); // ну и сохранили сюдой
        // ошибки должны обработаться внутри

    }
}
