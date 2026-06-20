package org.example._555laba555.ui.commands;

import org.example._555laba555.domain.Reagent;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

public class ReagentDelUI {
    private final ServiceManager services;

    public ReagentDelUI(ServiceManager services) {
        this.services = services;
    }

    public void execute(long reagentId) {
        Long login = services.getUserService().getCurrentUser().getId();
        Reagent reagent = services.getReagentService().getReagentById(reagentId);

        if (reagent == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Реактив не найден");
            alert.setContentText("Реактив с ID " + reagentId + " не существует.");
            alert.showAndWait();
            return;
        }

        long ownerId = reagent.getOwnerId();
        if (ownerId == login) { // случай где удалаляем
            CommandHistory history = new CommandHistory("delete", "reagent");
            history.setDeletedObject(copyReagent(reagent));
            history.setObjectId(reagentId);
            services.pushHistory(history);

            services.getReagentService().remove(reagentId);
        } else {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Доступ запрещен");
            alert.setContentText("Вы не являетесь владельцем этого реактива и не можете его удалить.");
            alert.showAndWait();
        }
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