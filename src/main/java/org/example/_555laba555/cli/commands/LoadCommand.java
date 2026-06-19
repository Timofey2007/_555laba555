package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;

public class LoadCommand implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) {
        System.out.println("Данные автоматически загружаются из базы данных при старте");
    }
}