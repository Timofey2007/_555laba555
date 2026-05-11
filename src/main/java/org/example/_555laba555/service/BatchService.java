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


public class BatchService {
    /** Хранилище партий: ключ - ID, значение - объект ReagentBatch */
    private Map<Long, ReagentBatch> items = new HashMap<>();

    private long nextId = 1;


    public ReagentBatch add(ReagentBatch batch) {
        batch.setId(nextId++);
        batch.setCreatedAt(Instant.now());
        batch.setUpdatedAt(Instant.now());

        BatchValidator.validate(batch);

        items.put(batch.getId(), batch);
        return batch;
    }


    public ReagentBatch getById(long id) {
        return items.get(id);
    }


    public List<ReagentBatch> getAll() {
        return new ArrayList<>(items.values());
    }


    public List<ReagentBatch> getByReagentId(long reagentId) {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch b : items.values()) {
            if (b.getReagentId() == reagentId) {
                result.add(b);
            }
        }
        return result;
    }

    public void update(ReagentBatch batch) {
        if (!items.containsKey(batch.getId())) {
            throw new ValidationException("Партия с ID " + batch.getId() + " не найдена");
        }
        batch.setUpdatedAt(Instant.now());
        BatchValidator.validate(batch);
        items.put(batch.getId(), batch);
    }


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


    public boolean exists(long id) {
        return items.containsKey(id);
    }


    public void loadFromList(List<ReagentBatch> list) {
        items.clear();  // очищаем старые данные
        for (ReagentBatch b : list) {
            items.put(b.getId(), b);
            // обновляем счетчик ID, чтобы новые объекты не пересекались
            if (b.getId() >= nextId) {
                nextId = b.getId() + 1;
            }
        }
    }
    public void remove(long id) {
        if (!items.containsKey(id)) {
            throw new ValidationException("Партия с ID " + id + " не найдена");
        }
        items.remove(id);
    }
}