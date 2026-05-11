package org.example._555laba555.service;

import org.example._555laba555.domain.Reagent;
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

    private Map<Long, StockMove> items = new HashMap<>();

    private long nextId = 1;


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

    public List<StockMove> getByBatchId(long batchId) {
        List<StockMove> result = new ArrayList<>();
        for (StockMove m : items.values()) {
            if (m.getBatchId() == batchId) {
                result.add(m);
            }
        }
        return result;
    }

    public List<StockMove> getByBatchId(long batchId, int limit) { // любой элемент типа Comparator или лямбда выражение
        List<StockMove> all = getByBatchId(batchId);
        all.sort( (a, b) -> { // в функцию sort() подставляется лямбда выражения или элементы типа Comparator<E>
            if (a.getMovedAt() == null) return 1;
            if (b.getMovedAt() == null) return -1;
            return b.getMovedAt().compareTo(a.getMovedAt());
        } );

        if (limit < all.size()) {
            return all.subList(0, limit);
        }
        return all;
    }

    public ArrayList<StockMove> getAll() {
        return new ArrayList<>(items.values());
    }


    public void loadFromList(List<StockMove> list) {
        items.clear();  // очищаем старые данные
        for (StockMove m : list) {
            items.put(m.getId(), m);
            // обновляем счетчик ID, чтобы новые объекты не пересекались
            if (m.getId() >= nextId) {
                nextId = m.getId() + 1;
            }
        }
    }
    public void remove(long id) {
        if (!items.containsKey(id)) {
            throw new ValidationException("Партия с ID " + id + " не найдена");
        }
        items.remove(id);
    }
    public StockMove getMoveById(long id){
        return items.get(id);
    }
}