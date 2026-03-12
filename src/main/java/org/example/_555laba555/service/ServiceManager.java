package org.example._555laba555.service;

import java.util.ArrayList;

/**
 * Менеджер сервисов - объединяет все сервисы в одном месте.
 * Упрощает передачу зависимостей между компонентами программы.
 * Предоставляет единую точку доступа к сервисам реактивов, партий и движений.
 *
 * @author Студент
 * @version 1.0
 */
public class ServiceManager {
    /** Сервис для работы с реактивами */
    private final ReagentService reagentService;

    /** Сервис для работы с партиями */
    private final BatchService batchService;

    /** Сервис для работы с движениями */
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
     *
     * @return сервис реактивов
     */
    public ReagentService getReagentService() {
        return reagentService;
    }

    /**
     * Возвращает сервис для работы с партиями.
     *
     * @return сервис партий
     */
    public BatchService getBatchService() {
        return batchService;
    }

    /**
     * Возвращает сервис для работы с движениями.
     *
     * @return сервис движений
     */
    public MoveService getMoveService() {
        return moveService;
    }

    /**
     * Очищает все данные во всех сервисах.
     * Используется при команде clear_data.
     */
    public void clearAll() {
        reagentService.loadFromList(new ArrayList<>());
        batchService.loadFromList(new ArrayList<>());
        moveService.loadFromList(new ArrayList<>());
    }
}