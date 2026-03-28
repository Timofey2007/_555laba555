package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

public class ReagAddCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        Reagent r = new Reagent();
        r.setName(input.readString("Название: ", true));
        r.setFormula(input.readString("Формула: ", true));
        r.setCas(input.readOptional("CAS: "));
        r.setHazardClass(input.readOptional("Класс опасности: "));
        r.setOwnerUsername(input.readString("Владелец: ", true));

        services.getReagentService().add(r);
        System.out.println("Реактив добавлен. ID: " + r.getId());
    }
}