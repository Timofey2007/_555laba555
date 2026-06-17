package org.example._555laba555.service;

import org.example._555laba555.dataBase.BatchRepository;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.BatchStatus;
import org.example._555laba555.validation.BatchValidator;
import org.example._555laba555.validation.ValidationException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchService {
    private final BatchRepository repository;
    private final Map<Long, ReagentBatch> cache = new HashMap<>();

    public BatchService(BatchRepository repository) {
        this.repository = repository;
        loadFromDatabase();
    }
    public BatchService() {
        this.repository = null;
    }

    private void loadFromDatabase() {
        try {
            cache.clear();
            for (ReagentBatch b : repository.findAll()) {
                cache.put(b.getId(), b);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки партий из БД: " + e.getMessage());
        }
    }

    public ReagentBatch add(ReagentBatch batch) throws ValidationException {
        try {
            BatchValidator.validate(batch);
            batch.setCreatedAt(Instant.now());
            batch.setUpdatedAt(Instant.now());
            repository.insert(batch);
            cache.put(batch.getId(), batch);
            return batch;
        } catch (SQLException e) {
            throw new ValidationException("Ошибка БД при добавлении партии: " + e.getMessage());
        }
    }

    public List<ReagentBatch> getAll() {
        return new ArrayList<>(cache.values());
    }

    public ReagentBatch getById(long id) {
        return cache.get(id);
    }

    public List<ReagentBatch> getByReagentId(long reagentId) {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch b : cache.values()) {
            if (b.getReagentId() == reagentId) {
                result.add(b);
            }
        }
        return result;
    }

    public void update(ReagentBatch batch) throws ValidationException {
        try {
            BatchValidator.validate(batch);
            batch.setUpdatedAt(Instant.now());
            repository.update(batch);
            cache.put(batch.getId(), batch);
        } catch (SQLException e) {
            throw new ValidationException("Ошибка обновления партии: " + e.getMessage());
        }
    }

    public void archive(long id) throws ValidationException {
        ReagentBatch batch = cache.get(id);
        if (batch == null) {
            throw new ValidationException("Партия не найдена");
        }
        if (batch.getStatus() == BatchStatus.ARCHIVED) {
            throw new ValidationException("Партия уже в архиве");
        }
        batch.setStatus(BatchStatus.ARCHIVED);
        batch.setUpdatedAt(Instant.now());
        try {
            repository.update(batch);
            cache.put(batch.getId(), batch);
        } catch (SQLException e) {
            throw new ValidationException("Ошибка архивации: " + e.getMessage());
        }
    }

    public void remove(long id) throws ValidationException {
        try {
            repository.delete(id);
            cache.remove(id);
        } catch (SQLException e) {
            throw new ValidationException("Ошибка удаления партии: " + e.getMessage());
        }
    }
    public void loadFromList(List<ReagentBatch> list) {
        cache.clear();
        for (ReagentBatch b : list) {
            cache.put(b.getId(), b);
        }
    }
}