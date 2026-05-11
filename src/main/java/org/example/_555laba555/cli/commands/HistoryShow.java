package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.service.CommandHistory;
import org.example._555laba555.service.ServiceManager;

import java.util.List;

public class HistoryShow implements Command {
    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
        List<CommandHistory> historyList = services.getHistory();

        if (historyList.isEmpty()) return;
        for(CommandHistory jj : historyList){
            System.out.println(jj);
        }
    }
}
