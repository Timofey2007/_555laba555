package org.example._555laba555.service;

import org.example._555laba555.dataBase.MoveRepository;
import org.example._555laba555.domain.StockMove;
import org.example._555laba555.validation.MoveValidator;
import org.example._555laba555.validation.ValidationException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveService {
    private final MoveRepository repository;
    private final Map<Long, StockMove> cache = new HashMap<>();

    public MoveService(MoveRepository repository) {
        this.repository = repository;
        loadFromDatabase();
    }

    public MoveService() {
        this.repository = null;
    }

    private void loadFromDatabase() {
        try {
            cache.clear();
            for (StockMove m : repository.findAll()) {
                cache.put(m.getId(), m);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки движений из БД: " + e.getMessage());
        }
    }

    public StockMove add(StockMove move, double currentQuantity) throws ValidationException {
        try {
            MoveValidator.validate(move);
            MoveValidator.checkQuantity(move, currentQuantity);
            if (move.getMovedAt() == null) {
                move.setMovedAt(Instant.now());
            }
            move.setCreatedAt(Instant.now());
            repository.insert(move);
            cache.put(move.getId(), move);
            return move;
        } catch (SQLException e) {
            throw new ValidationException("Ошибка БД при добавлении движения: " + e.getMessage());
        }
    }

    public List<StockMove> getAll() {
        return new ArrayList<>(cache.values());
    }

    public StockMove getById(long id) {
        return cache.get(id);
    }

    /**
     * Получить движение по ID
     * @param id ID движения
     * @return объект StockMove или null
     */
    public StockMove getMoveById(long id) {
        return cache.get(id);
    }

    public List<StockMove> getByBatchId(long batchId) {
        List<StockMove> result = new ArrayList<>();
        for (StockMove m : cache.values()) {
            if (m.getBatchId() == batchId) {
                result.add(m);
            }
        }
        return result;
    }

    public List<StockMove> getByBatchId(long batchId, int limit) {
        List<StockMove> all = getByBatchId(batchId);
        all.sort((a, b) -> {
            if (a.getMovedAt() == null) return 1;
            if (b.getMovedAt() == null) return -1;
            return b.getMovedAt().compareTo(a.getMovedAt());
        });
        if (limit < all.size()) {
            return all.subList(0, limit);
        }
        return all;
    }

    public void remove(long id) throws ValidationException {
        try {
            repository.delete(id);
            cache.remove(id);
        } catch (SQLException e) {
            throw new ValidationException("Ошибка удаления движения: " + e.getMessage());
        }
    }

    public void loadFromList(List<StockMove> list) {
        cache.clear();
        for (StockMove m : list) {
            cache.put(m.getId(), m);
        }
    }
}