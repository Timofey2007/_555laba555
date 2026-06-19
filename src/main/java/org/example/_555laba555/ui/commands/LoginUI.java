package org.example._555laba555.ui.commands;

import org.example._555laba555.service.ServiceManager;

public class LoginUI {
    private final ServiceManager services;

    public LoginUI(ServiceManager services) {
        this.services = services;
    }

    public void execute(String login, String password) throws Exception {
        if (services.getUserService().isAuthenticated()) {
            throw new Exception("Вы уже авторизованы");
        }

        services.getUserService().login(login, password);
    }
}