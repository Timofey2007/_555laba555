package org.example._555laba555.service;

import org.example._555laba555.fileManager.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ServiceManager {
    private final ReagentService reagentService;

    private final BatchService batchService;

    private final MoveService moveService;

    private final UserService userService;

    private final UserStorage userStorage;

    private final Stack<CommandHistory> history = new Stack<>(); // удобство использования Stack при работе с историей заключается в том, что все элементы стэка грубо говоря складываются друг на друга и автоматически работа ведется только с последним элементом


    public ServiceManager() {
        this.reagentService = new ReagentService();
        this.batchService = new BatchService();
        this.moveService = new MoveService();
        this.userService = new UserService();
        this.userStorage = new UserStorage();
    }
    // сервисы для работы с файловой составляющей

    public ReagentService getReagentService() {
        return reagentService;
    }

    public BatchService getBatchService() {
        return batchService;
    }
    public UserService getUserService() {
        return userService;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    // сохраняем пользователей
    public void saveUsers() {
        userStorage.saveUsers(userService.getAllUsers());
    }


    public MoveService getMoveService() {
        return moveService;
    }

    public void pushHistory(CommandHistory cmd) {
        history.push(cmd); // добавляет действие совершенное
    }
    public CommandHistory popHistory() {
        return history.isEmpty() ? null : history.pop(); // обращается к последнему элементу и удаляет его после чего переходит к следующему в стэке
    }

    public boolean isHistoryEmpty() {
        return history.isEmpty();
    }
    public List<CommandHistory> getHistory() {
        return new ArrayList<>(history);  // возвращаем копию чтобы не нарушить порядок
    }
}