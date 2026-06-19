package org.example._555laba555.service;

import org.example._555laba555.dataBase.*;
import java.util.Stack;

public class ServiceManager {
    private final ReagentService reagentService;
    private final BatchService batchService;
    private final MoveService moveService;
    private final UserService userService;
    private final Stack<CommandHistory> historyStack = new Stack<>();

    public ServiceManager() {
        UserRepository userRepo = new UserRepository();
        ReagentRepository reagentRepo = new ReagentRepository();
        BatchRepository batchRepo = new BatchRepository();
        MoveRepository moveRepo = new MoveRepository();

        this.userService = new UserService(userRepo);
        this.reagentService = new ReagentService(reagentRepo);
        this.batchService = new BatchService(batchRepo);
        this.moveService = new MoveService(moveRepo);

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        userService.loadAll();
        reagentService.loadAll();
        batchService.loadAll();
        moveService.loadAll();
    }

    public ReagentService getReagentService() { return reagentService; }
    public BatchService getBatchService() { return batchService; }
    public MoveService getMoveService() { return moveService; }
    public UserService getUserService() { return userService; }

    public void pushHistory(CommandHistory history) { historyStack.push(history); }
    public CommandHistory popHistory() { return historyStack.isEmpty() ? null : historyStack.pop(); }
    public boolean isHistoryEmpty() { return historyStack.isEmpty(); }
}