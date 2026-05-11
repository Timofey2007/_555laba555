package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.StockMove;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;


public class DeletingOfDeleting implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args){
        if(services.isHistoryEmpty()) return;

        CommandHistory ch = services.popHistory(); // это объект обладающий свойставми последнего удаленного элемента
        String typeOfDeleted = ch.getTypeOfDeleted();
        Object deletedObject = ch.getDeletedObject();

        try{
            switch (typeOfDeleted){
                case("move"):
                    if (deletedObject instanceof StockMove) {
                        services.getMoveService().add((StockMove) deletedObject, Double.MAX_VALUE);
                    }
                    break;
                case ("reagent"):
                    if (deletedObject instanceof Reagent) {
                        services.getReagentService().add((Reagent) deletedObject);
                    }
                    break;
                case ("batch"):
                    if (deletedObject instanceof ReagentBatch) {
                        services.getBatchService().add((ReagentBatch) deletedObject);
                    }
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("при восстановлении что то пошло не так");
        }


    }

}
