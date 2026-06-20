package org.example._555laba555.service;

import org.example._555laba555.dataBase.*;

import java.time.LocalDateTime;
import java.util.Stack;

import static org.apache.commons.lang3.SerializationUtils.serialize;

public class ServiceManager {
    private final ReagentService reagentService;
    private final BatchService batchService;
    private final MoveService moveService;
    private final UserService userService;
    private final Stack<CommandHistory> historyStack = new Stack<>(); //в краткосрочной памяти все равно будут храниться удаленные объекты
    private final HistoryRepository hisRepo;

    public ServiceManager() {
        UserRepository userRepo = new UserRepository();
        ReagentRepository reagentRepo = new ReagentRepository();
        BatchRepository batchRepo = new BatchRepository();
        MoveRepository moveRepo = new MoveRepository();

        this.userService = new UserService(userRepo);
        this.reagentService = new ReagentService(reagentRepo);
        this.batchService = new BatchService(batchRepo);
        this.moveService = new MoveService(moveRepo);
        this.hisRepo = new HistoryRepository();

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        userService.loadAll();
        reagentService.loadAll();
        batchService.loadAll();
        moveService.loadAll();
    }

    public ReagentService getReagentService() {
        return reagentService;
    }

    public BatchService getBatchService() { return batchService; }
    public MoveService getMoveService() { return moveService; }
    public UserService getUserService() { return userService; }

    public void pushHistory(CommandHistory history) {
        historyStack.push(history);

        try {
            HistoryRecord record = new HistoryRecord();
            record.setObjectType(history.getTypeOfDeleted());
            record.setObjectId(history.getObjectId());
            // не разобрался как засетить объект через геттер из команд хистори
            record.setDeletedBy(userService.getCurrentUserId());
            record.setDeletedAt(LocalDateTime.now());
            hisRepo.save(record);
            System.out.println("История сохранена в БД: " + history.getTypeOfDeleted() + " ID=" + history.getObjectId());
        } catch (Exception e) {
            System.err.println("Не удалось сохранить историю в БД: " + e.getMessage());
        }
    }

    public CommandHistory popHistory() { return historyStack.isEmpty() ? null : historyStack.pop(); }
    public boolean isHistoryEmpty() { return historyStack.isEmpty(); }
}