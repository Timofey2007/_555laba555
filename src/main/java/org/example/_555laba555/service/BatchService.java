package org.example._555laba555.service;

import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.BatchStatus;
import org.example._555laba555.dataBase.BatchRepository;
import org.example._555laba555.validation.BatchValidator;
import org.example._555laba555.validation.ValidationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchService {
    private final Map<Long, ReagentBatch> items = new HashMap<>();
    private final BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public ReagentBatch add(ReagentBatch batch) {
        BatchValidator.validate(batch);
        batch.setCreatedAt(Instant.now());
        batch.setUpdatedAt(Instant.now());
        long generatedId = batchRepository.save(batch);
        batch.setId(generatedId);
        items.put(generatedId, batch);
        return batch;
    }

    public void updateQuantity(long batchId, double newQuantity) {
        ReagentBatch batch = items.get(batchId);
        if (batch == null) {
            throw new ValidationException("Партия не найдена");
        }
        if (newQuantity < 0) {
            throw new ValidationException("Количество не может быть отрицательным");
        }
        batchRepository.updateQuantity(batchId, newQuantity, Instant.now());
        batch.setQuantityCurrent(newQuantity);
        batch.setUpdatedAt(Instant.now());
    }

    // ДОБАВЛЕН МЕТОД UPDATE ДЛЯ ПОЛНОГО ОБНОВЛЕНИЯ (используется в BatchUpdateCommand)
    public void update(ReagentBatch batch) {
        batch.setUpdatedAt(Instant.now());
        // В текущей реализации репозитория нет полного update, но мы обновляем кэш
        // Если нужно сохранять в БД все поля, нужно добавить метод update в BatchRepository
        items.put(batch.getId(), batch);
    }

    public void archive(long id) {
        ReagentBatch batch = items.get(id);
        if (batch == null) throw new ValidationException("Партия не найдена");
        batch.setStatus(BatchStatus.ARCHIVED);
        batch.setUpdatedAt(Instant.now());
        items.put(id, batch);
    }

    public void remove(long id) {
        items.remove(id);
        // В репозитории пока нет delete для партий, добавим если нужно
    }

    public void loadAll() {
        try {
            items.clear();
            List<ReagentBatch> fromDb = batchRepository.findAll();
            for (ReagentBatch b : fromDb) {
                items.put(b.getId(), b);
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки партий: " + e.getMessage());
        }
    }

    public List<ReagentBatch> getAll() {
        return new ArrayList<>(items.values());
    }

    // ✅ ДОБАВЛЕН НЕДОСТАЮЩИЙ МЕТОД
    public List<ReagentBatch> getByReagentId(long reagentId) {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch b : items.values()) {
            if (b.getReagentId() == reagentId) {
                result.add(b);
            }
        }
        return result;
    }

    public ReagentBatch getById(long id) {
        return items.get(id);
    }
}