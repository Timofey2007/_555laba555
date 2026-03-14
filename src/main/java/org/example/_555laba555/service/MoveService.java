package org.example._555laba555.service;

import org.example._555laba555.domain.StockMove;
import org.example._555laba555.validation.MoveValidator;
import org.example._555laba555.validation.ValidationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления историей движений.
 * Отвечает за добавление, поиск и сортировку движений по партиям.
 * Хранит данные в HashMap
 */
public class MoveService {
    /**
     * Хранилище движений: ключ - ID, значение - объект StockMove
     */
    private Map<Long, StockMove> items = new HashMap<>();

    private long nextId = 1;

    /**
     * Добавляет новое движение в историю.
     * Генерирует ID, проверяет достаточно ли реактива для расхода,
     * устанавливает время создания.
     */
    public StockMove add(StockMove move, double currentQuantity) {
        MoveValidator.validate(move);
        MoveValidator.checkQuantity(move, currentQuantity);

        move.setId(nextId++);
        if (move.getMovedAt() == null) {
            move.setMovedAt(Instant.now());
        }
        move.setCreatedAt(Instant.now());

        items.put(move.getId(), move);
        return move;
    }
    /**
     * Возвращает список движений для указанной партии.
     */
    public List<StockMove> getByBatchId(long batchId) {
        List<StockMove> result = new ArrayList<>();
        for (StockMove m : items.values()) {
            if (m.getBatchId() == batchId) {
                result.add(m);
            }
        }
        return result;
    }
    /**
     * Возвращает список последних движений для указанной партии.
     * Движения сортируются от новых к старым.
     */
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
}