package org.example._555laba555.service;

import org.example._555laba555.dataBase.DataBaseManager;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.domain.BatchStatus;
import org.example._555laba555.dataBase.BatchRepository;
import org.example._555laba555.validation.BatchValidator;
import org.example._555laba555.validation.StorageException;
import org.example._555laba555.validation.ValidationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    public void update(ReagentBatch batch) {
        String sql = "UPDATE reagent_batches SET label = ?, quantity_current = ?, unit = ?, " +
                "location = ?, expires_at = ?, reagent_id = ? WHERE id = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, batch.getLabel());
            pstmt.setDouble(2, batch.getQuantityCurrent());
            pstmt.setString(3, batch.getUnit().name());
            pstmt.setString(4, batch.getLocation());
            pstmt.setTimestamp(5, batch.getExpiresAt() != null ? Timestamp.from(batch.getExpiresAt()) : null);
            pstmt.setLong(6, batch.getReagentId());
            pstmt.setLong(7, batch.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new StorageException("Партия с ID " + batch.getId() + " не найдена для обновления", null);
            }
        } catch (SQLException e) {
            throw new StorageException("Ошибка БД при обновлении партии: " + e.getMessage(), e);
        }
    }
    public void archive(long id) {
        ReagentBatch batch = items.get(id);
        if (batch == null) throw new ValidationException("Партия не найдена");
        batch.setStatus(BatchStatus.ARCHIVED);
        batch.setUpdatedAt(Instant.now());
        items.put(id, batch);
    }

    public void remove(long id) {
        batchRepository.delete(id);
        items.remove(id);
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
        try {
            List<ReagentBatch> fromDb = batchRepository.findAll();
            items.clear();
            for (ReagentBatch b : fromDb) {
                items.put(b.getId(), b);
            }
            return new ArrayList<>(items.values());
        } catch (StorageException e) {
            System.err.println("Ошибка получения данных из БД: " + e.getMessage());
            return new ArrayList<>(items.values());
        }
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

    public ReagentBatch getById(long id) {
        return items.get(id);
    }
}