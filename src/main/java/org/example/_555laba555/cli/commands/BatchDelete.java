package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

import java.util.Scanner;

public class BatchDelete implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args){
        if (args == null || args.trim().isEmpty()) return;
        long batchId;
        try{
            batchId = Long.parseLong(args.trim());

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        ReagentBatch batch = services.getBatchService().getById(batchId);
        if(batch == null) return;
        System.out.println("Вы собираетесь удалить партию " + batch.getId()+ ":" +
                batch + "\nУверены, что хотите удалить эту партию? Y/N"); // оказывается toString() вызывается автоматически

        try(Scanner scanner = new Scanner(System.in)){
            String consInput = scanner.nextLine().trim();
            if(!consInput.equalsIgnoreCase("Y")){
                System.out.println("вы отменили удаление");
                return;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CommandHistory history = new CommandHistory("delete", "batch");
        history.setDeletedObject(copyBatch(batch));
        history.setObjectId(batchId);
        services.pushHistory(history);


        services.getBatchService().remove(batchId);
    }
    private ReagentBatch copyBatch(ReagentBatch original) {
        ReagentBatch copy = new ReagentBatch();
        copy.setId(original.getId());
        copy.setReagentId(original.getReagentId());
        copy.setLabel(original.getLabel());
        copy.setQuantityCurrent(original.getQuantityCurrent());
        copy.setUnit(original.getUnit());
        copy.setLocation(original.getLocation());
        copy.setExpiresAt(original.getExpiresAt());
        copy.setStatus(original.getStatus());
        copy.setOwnerUsername(original.getOwnerUsername());
        copy.setCreatedAt(original.getCreatedAt());
        copy.setUpdatedAt(original.getUpdatedAt());
        return copy;
    }


}
