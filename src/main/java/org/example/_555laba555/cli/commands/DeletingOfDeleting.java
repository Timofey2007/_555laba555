package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.StockMove;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

public class DeletingOfDeleting implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input, String args) {
        if (services.isHistoryEmpty()) {
            System.out.println("История пуста");
            return;
        }

        CommandHistory ch = services.popHistory();
        String typeOfDeleted = ch.getTypeOfDeleted();
        Object deletedObject = ch.getDeletedObject();

        try {
            switch (typeOfDeleted) {
                case "move":
                    if (deletedObject instanceof StockMove) {
                        services.getMoveService().add((StockMove) deletedObject, Double.MAX_VALUE);
                        System.out.println("Движение восстановлено");
                    }
                    break;
                case "reagent":
                    if (deletedObject instanceof Reagent) {
                        services.getReagentService().add((Reagent) deletedObject);
                        System.out.println("Реактив восстановлен");
                    }
                    break;
                case "batch":
                    if (deletedObject instanceof ReagentBatch) {
                        services.getBatchService().add((ReagentBatch) deletedObject);
                        System.out.println("Партия восстановлена");
                    }
                    break;
                default:
                    System.out.println("Неизвестный тип объекта");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при восстановлении: " + e.getMessage());
        }
    }
}