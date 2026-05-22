package org.example._555laba555.ui.commands;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.service.ServiceManager;

public class ReagAddUi {
    private final ServiceManager services;

    public ReagAddUi(ServiceManager services) {
        this.services = services;
    }
    public void execute(String name, String formula, String cas, String hazardClass) {
        Reagent reagent = new Reagent();

        reagent.setName(name);
        reagent.setFormula(formula);
        reagent.setCas(cas);
        reagent.setHazardClass(hazardClass);

        services.getReagentService().add(reagent);

    }
}
