package org.example._555laba555.service;

import org.example._555laba555.domain.StockMove;
import org.example._555laba555.dataBase.MoveRepository;
import org.example._555laba555.validation.MoveValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveService {
    private final Map<Long, StockMove> items = new HashMap<>();
    private final MoveRepository repository;

    public MoveService(MoveRepository repository) {
        this.repository = repository;
    }

    public void loadAll() {
        items.clear();
        List<StockMove> fromDb = repository.findAll();
        for (StockMove m : fromDb) {
            items.put(m.getId(), m);
        }
    }

    public StockMove add(StockMove move, double currentQuantity) {
        MoveValidator.validate(move);
        MoveValidator.checkQuantity(move, currentQuantity);
        long generatedId = repository.save(move);
        move.setId(generatedId);
        items.put(generatedId, move);
        return move;
    }

    public List<StockMove> getAll() { return new ArrayList<>(items.values()); }
    public StockMove getMoveById(long id) { return items.get(id); }

    public List<StockMove> getByBatchId(long batchId, int limit) {
        List<StockMove> all = new ArrayList<>();
        for (StockMove m : items.values()) {
            if (m.getBatchId() == batchId) all.add(m);
        }
        all.sort((a, b) -> b.getMovedAt().compareTo(a.getMovedAt()));
        return all.subList(0, Math.min(limit, all.size()));
    }

    public void remove(long id) {
        repository.delete(id);
        items.remove(id);
    }
}