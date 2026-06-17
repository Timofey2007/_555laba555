package org.example._555laba555.service;

import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.dataBase.DatabaseStorage;
import org.example._555laba555.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ServiceManager {
    private final ReagentService reagentService;
    private final BatchService batchService;
    private final MoveService moveService;
    private final UserService userService;
    private final Storage storage;

    private final Stack<CommandHistory> history = new Stack<>();

    public ServiceManager(boolean useDatabase) {
        this.reagentService = new ReagentService();
        this.batchService = new BatchService();
        this.moveService = new MoveService();
        this.userService = new UserService(null);

        if (useDatabase) {
            this.storage = new DatabaseStorage();
            System.out.println("Режим: PostgreSQL");
        } else {
            this.storage = new Conservation();
            System.out.println("Режим: CSV файл");
        }

        try {
            storage.load(this);
        } catch (Exception e) {
            System.out.println("Не удалось загрузить данные: " + e.getMessage());
        }
    }

    public ServiceManager() {
        this(false);
    }

    public ReagentService getReagentService() { return reagentService; }
    public BatchService getBatchService() { return batchService; }
    public MoveService getMoveService() { return moveService; }
    public UserService getUserService() { return userService; }
    public Storage getStorage() { return storage; }

    /**
     * Добавляет команду в историю.
     * Вызывается перед выполнением операции, которую можно отменить.
     */
    public void pushHistory(CommandHistory cmd) {
        history.push(cmd);
    }


    public CommandHistory popHistory() {
        return history.isEmpty() ? null : history.pop();
    }


    public boolean isHistoryEmpty() {
        return history.isEmpty();
    }

    public List<CommandHistory> getHistory() {
        return new ArrayList<>(history);
    }


    public void saveAll() {
        try {
            storage.save(this);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    /**
     * Сохраняет пользователей (для файлового режима)
     * В БД-режиме пользователи уже сохранены
     */
    public void saveUsers() {
        userService.saveUsers();
    }
}