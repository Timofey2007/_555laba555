package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

import java.util.List;

public class ReagListCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        String query = null;
        if (args.contains("--q")) {
            String[] parts = args.split("--q");
            if (parts.length > 1) {
                query = parts[1].trim();
            }
        }

        List<Reagent> list = query != null ?
                services.getReagentService().searchByName(query) :
                services.getReagentService().getAll();

        if (list.isEmpty()) {
            System.out.println("Реактивы не найдены");
            return;
        }

        System.out.printf("%-5s %-20s %-10s %-15s%n", "ID", "Название", "Формула", "CAS");
        System.out.println("------------------------------------------------");
        for (Reagent r : list) {
            System.out.printf("%-5d %-20s %-10s %-15s%n",
                    r.getId(),
                    truncate(r.getName(), 20),
                    truncate(r.getFormula(), 10),
                    truncate(r.getCas(), 15));
        }
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }
}