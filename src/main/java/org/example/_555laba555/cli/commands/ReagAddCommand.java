package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;

public class ReagAddCommand implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) throws Exception {
        if (!services.getUserService().isAuthenticated()) {
            System.out.println("Ошибка: необходимо авторизоваться (команда 'login')");
            return;
        }

        Reagent r = new Reagent();
        r.setName(input.readString("Название: ", true));
        r.setFormula(input.readString("Формула: ", true));
        r.setCas(input.readOptional("CAS: "));
        r.setHazardClass(input.readOptional("Класс опасности: "));

        r.setOwnerId(services.getUserService().getCurrentUserId());
        r.setOwnerName(services.getUserService().getCurrentUserLogin());

        services.getReagentService().add(r);
        System.out.println("Реактив добавлен. ID: " + r.getId());
    }
}