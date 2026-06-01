package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;


/**
 * Команда регистрации нового пользователя
 */
public class RegisterCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        System.out.println("\nРЕГИСТРАЦИЯ НОВОГО ПОЛЬЗОВАТЕЛЯ");

        String login = input.readString("Логин: ", true);
        String password = input.readString("Пароль: ", true);

        services.getUserService().register(login, password);
        services.saveUsers(); // сохраняем после регистрации
    }
}