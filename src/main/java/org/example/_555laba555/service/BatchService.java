package org.example._555laba555.service;

import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.BatchStatus;
import org.example._555laba555.validation.BatchValidator;
import org.example._555laba555.validation.ValidationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления коллекцией партий реактивов.
 * Отвечает за добавление, поиск, обновление и архивацию партий.
 * Хранит данные в HashMap
 */
public class BatchService {
    /** Хранилище партий: ключ - ID, значение - объект ReagentBatch */
    private Map<Long, ReagentBatch> items = new HashMap<>();

    private long nextId = 1;

    /**
     * Добавляет новую партию в коллекцию.
     * Генерирует ID, устанавливает даты создания и обновления,
     */
    public ReagentBatch add(ReagentBatch batch) {
        batch.setId(nextId++);
        batch.setCreatedAt(Instant.now());
        batch.setUpdatedAt(Instant.now());

        BatchValidator.validate(batch);

        items.put(batch.getId(), batch);
        return batch;
    }

    /**
     * Возвращает партию по ее ID.
     */
    public ReagentBatch getById(long id) {
        return items.get(id);
    }

    /**
     * Возвращает список всех партий.
     */
    public List<ReagentBatch> getAll() {
        return new ArrayList<>(items.values());
    }

    /**
     * Возвращает список партий, относящихся к указанному реактиву.
     */
    public List<ReagentBatch> getByReagentId(long reagentId) {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch b : items.values()) {
            if (b.getReagentId() == reagentId) {
                result.add(b);
            }
        }
        return result;
    }
    /**
     * Обновляет существующую партию.
     */
    public void update(ReagentBatch batch) {
        if (!items.containsKey(batch.getId())) {
            throw new ValidationException("Партия с ID " + batch.getId() + " не найдена");
        }
        batch.setUpdatedAt(Instant.now());
        BatchValidator.validate(batch);
        items.put(batch.getId(), batch);
    }

    /**
     * Архивирует партию (меняет статус на ARCHIVED).
     */
    public void archive(long id) {
        ReagentBatch batch = items.get(id);
        if (batch == null) {
            throw new ValidationException("Партия с ID " + id + " не найдена");
        }
        if (batch.getStatus() == BatchStatus.ARCHIVED) {
            throw new ValidationException("Партия уже в архиве");
        }
        batch.setStatus(BatchStatus.ARCHIVED);
        batch.setUpdatedAt(Instant.now());
    }

    /**
     * Проверяет, существует ли партия с указанным ID.
     */
    public boolean exists(long id) {
        return items.containsKey(id);
    }
}