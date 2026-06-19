package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;

public class HistoryShow implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) throws Exception {
        System.out.println("История команд недоступна в этом режиме");
    }
}