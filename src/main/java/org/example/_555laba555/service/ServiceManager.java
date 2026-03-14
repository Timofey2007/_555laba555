package org.example._555laba555.service;

import java.util.ArrayList;

/**
 * Менеджер сервисов - объединяет все сервисы в одном месте.
 */
public class ServiceManager {
    private final ReagentService reagentService;

    private final BatchService batchService;

    private final MoveService moveService;

    /**
     * Создает менеджер и инициализирует все сервисы.
     */
    public ServiceManager() {
        this.reagentService = new ReagentService();
        this.batchService = new BatchService();
        this.moveService = new MoveService();
    }

    /**
     * Возвращает сервис для работы с реактивами.
     */
    public ReagentService getReagentService() {
        return reagentService;
    }

    /**
     * Возвращает сервис для работы с партиями.
     */
    public BatchService getBatchService() {
        return batchService;
    }

    /**
     * Возвращает сервис для работы с движениями.
     */
    public MoveService getMoveService() {
        return moveService;
    }

}