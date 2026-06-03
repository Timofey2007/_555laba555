package org.example._555laba555.ui.commands;

import org.example._555laba555.service.ServiceManager;

public class RegisterUI {
    private final ServiceManager services;

    public RegisterUI(ServiceManager services) {
        this.services = services;
    }

    public void execute(String login, String password) throws Exception {
        services.getUserService().register(login, password);
        services.saveUsers();
    }
}