package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

public class ReagentDelete implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) {
        if (!services.getUserService().isAuthenticated()) {
            System.out.println("Ошибка: необходимо авторизоваться (команда 'login')");
            return;
        }

        if (args == null || args.trim().isEmpty()) {
            System.out.println("Использование: reag_del <ID>");
            return;
        }

        long reagentId;
        try {
            reagentId = Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            return;
        }

        Reagent reagent = services.getReagentService().getReagentById(reagentId);
        if (reagent == null) {
            System.out.println("Реагент с ID " + reagentId + " не найден");
            return;
        }

        long currentUserId = services.getUserService().getCurrentUserId();
        if (reagent.getOwnerId() != currentUserId && !services.getUserService().isAdmin()) {
            System.out.println("Ошибка: у вас нет прав на удаление этого реактива");
            System.out.println("   Владелец: " + reagent.getOwnerName());
            return;
        }

        System.out.println("\nВЫ СОБИРАЕТЕСЬ УДАЛИТЬ РЕАГЕНТ:");
        System.out.println("   ID: " + reagent.getId());
        System.out.println("   Название: " + reagent.getName());
        System.out.println("   Владелец: " + reagent.getOwnerName());

        System.out.print("\nУверены, что хотите удалить? (Y/N): ");
        try {
            String confirm = input.readString("", true);
            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("Удаление отменено");
                return;
            }
        } catch (Exception e) {
            System.out.println("Ошибка ввода");
            return;
        }

        CommandHistory history = new CommandHistory("delete", "reagent");
        history.setDeletedObject(copyReagent(reagent));
        history.setObjectId(reagentId);
        services.pushHistory(history);

        services.getReagentService().remove(reagentId);
        System.out.println("\nРеагент удален. Введите 'cancel_del' для восстановления");
    }

    private Reagent copyReagent(Reagent original) {
        Reagent copy = new Reagent();
        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setFormula(original.getFormula());
        copy.setCas(original.getCas());
        copy.setHazardClass(original.getHazardClass());
        copy.setOwnerId(original.getOwnerId());
        copy.setOwnerName(original.getOwnerName());
        copy.setCreatedAt(original.getCreatedAt());
        copy.setUpdatedAt(original.getUpdatedAt());
        return copy;
    }
}