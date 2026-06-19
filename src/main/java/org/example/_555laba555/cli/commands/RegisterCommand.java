package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;

public class RegisterCommand implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) throws Exception {
        System.out.println("\nРЕГИСТРАЦИЯ НОВОГО ПОЛЬЗОВАТЕЛЯ");

        String login = input.readString("Логин: ", true);
        String password = input.readString("Пароль: ", true);

        if (services.getUserService().register(login, password)) {
            System.out.println("Пользователь '" + login + "' успешно зарегистрирован");
        } else {
            System.out.println("Ошибка регистрации: пользователь с таким логином уже существует");
        }
    }
}