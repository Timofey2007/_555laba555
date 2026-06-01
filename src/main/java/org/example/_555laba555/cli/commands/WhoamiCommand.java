package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.domain.User;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

/**
 * Команда получения информации о текущем пользователе
 */
public class WhoamiCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        if (!services.getUserService().isAuthenticated()) {
            System.out.println("Вы не авторизованы");
            return;
        }

        User user = services.getUserService().getCurrentUser();
        System.out.println("   Логин: " + user.getLogin());
        System.out.println("   Роль: " + user.getRole());
        System.out.println("   ID: " + user.getId());
        if (user.getLastLogin() != null) {
            System.out.println("   Последний вход: " + user.getLastLogin());
        }
    }
}