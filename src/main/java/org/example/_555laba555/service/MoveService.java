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
 * Хранит данные в HashMap (вариант 2 из задания).
 *
 * @author Студент
 * @version 1.0
 */
public class MoveService {
    /** Хранилище движений: ключ - ID, значение - объект StockMove */
    private Map<Long, StockMove> items = new HashMap<>();

    /** Следующий доступный ID для нового движения */
    private long nextId = 1;

    /**
     * Добавляет новое движение в историю.
     * Генерирует ID, проверяет достаточно ли реактива для расхода,
     * устанавливает время создания.
     *
     * @param move движение для добавления
     * @param currentQuantity текущее количество в партии (для проверки)
     * @return добавленное движение с установленным ID
     * @throws ValidationException если данные не прошли валидацию
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
     * Возвращает движение по его ID.
     *
     * @param id идентификатор движения
     * @return движение или null, если не найдено
     */
    public StockMove getById(long id) {
        return items.get(id);
    }

    /**
     * Возвращает список всех движений.
     *
     * @return список всех движений
     */
    public List<StockMove> getAll() {
        return new ArrayList<>(items.values());
    }

    /**
     * Возвращает список движений для указанной партии.
     *
     * @param batchId ID партии
     * @return список движений для данной партии
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
     *
     * @param batchId ID партии
     * @param limit максимальное количество возвращаемых движений
     * @return список последних движений (не больше limit)
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

    /**
     * Загружает движения из списка (используется при чтении из файла).
     * Очищает текущую коллекцию и заполняет новыми данными.
     *
     * @param list список движений для загрузки
     */
    public void loadFromList(List<StockMove> list) {
        items.clear();
        for (StockMove m : list) {
            items.put(m.getId(), m);
            if (m.getId() >= nextId) {
                nextId = m.getId() + 1;
            }
        }
    }

    /**
     * Возвращает список всех движений для сохранения в файл.
     *
     * @return список движений
     */
    public List<StockMove> getList() {
        return new ArrayList<>(items.values());
    }
}