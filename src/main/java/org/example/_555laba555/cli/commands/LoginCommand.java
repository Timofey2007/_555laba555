package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

import java.io.IOException;

/**
 * Команда входа в систему
 */
    public class LoginCommand implements Command {

        @Override
        public void justDOIT(ServiceManager services, InputHelper input,
                             Conservation storage, String args) throws Exception {
            if (services.getUserService().isAuthenticated()) {
                System.out.println("Вы уже авторизованы");
                return;
            }

            System.out.println("\nВХОД В СИСТЕМУ");
            String login = input.readString("Логин: ", true);
            String password = input.readString("Пароль: ", true);

            services.getUserService().login(login, password);
            services.saveUsers(); // обновляем lastLogin
        }
    }