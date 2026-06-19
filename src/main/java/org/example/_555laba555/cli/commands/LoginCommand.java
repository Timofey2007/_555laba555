package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;

public class LoginCommand implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) throws Exception {
        if (services.getUserService().isAuthenticated()) {
            System.out.println("Вы уже авторизованы");
            return;
        }

        System.out.println("\nВХОД В СИСТЕМУ");
        String login = input.readString("Логин: ", true);
        String password = input.readString("Пароль: ", true);

        if (services.getUserService().login(login, password)) {
            System.out.println("Добро пожаловать, " + login + "!");
        } else {
            System.out.println("Ошибка входа: неверный логин или пароль");
        }
    }
}