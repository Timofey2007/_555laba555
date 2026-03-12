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
 * Хранит данные в HashMap (вариант 2 из задания).
 *
 * @author Студент
 * @version 1.0
 */
public class BatchService {
    /** Хранилище партий: ключ - ID, значение - объект ReagentBatch */
    private Map<Long, ReagentBatch> items = new HashMap<>();

    /** Следующий доступный ID для новой партии */
    private long nextId = 1;

    /**
     * Добавляет новую партию в коллекцию.
     * Генерирует ID, устанавливает даты создания и обновления,
     * выполняет валидацию перед сохранением.
     *
     * @param batch партия для добавления
     * @return добавленная партия с установленным ID
     * @throws ValidationException если данные не прошли валидацию
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
     *
     * @param id идентификатор партии
     * @return партия или null, если не найдена
     */
    public ReagentBatch getById(long id) {
        return items.get(id);
    }

    /**
     * Возвращает список всех партий.
     *
     * @return список всех партий
     */
    public List<ReagentBatch> getAll() {
        return new ArrayList<>(items.values());
    }

    /**
     * Возвращает список партий, относящихся к указанному реактиву.
     *
     * @param reagentId ID реактива
     * @return список партий для данного реактива
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
     * Возвращает список только активных партий.
     *
     * @return список партий со статусом ACTIVE
     */
    public List<ReagentBatch> getActive() {
        List<ReagentBatch> result = new ArrayList<>();
        for (ReagentBatch b : items.values()) {
            if (b.getStatus() == BatchStatus.ACTIVE) {
                result.add(b);
            }
        }
        return result;
    }

    /**
     * Обновляет существующую партию.
     *
     * @param batch партия с обновленными данными
     * @throws ValidationException если партия не найдена или данные невалидны
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
     *
     * @param id идентификатор партии
     * @throws ValidationException если партия не найдена или уже в архиве
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
     *
     * @param id проверяемый идентификатор
     * @return true если партия существует
     */
    public boolean exists(long id) {
        return items.containsKey(id);
    }

    /**
     * Загружает партии из списка (используется при чтении из файла).
     * Очищает текущую коллекцию и заполняет новыми данными.
     *
     * @param list список партий для загрузки
     */
    public void loadFromList(List<ReagentBatch> list) {
        items.clear();
        for (ReagentBatch b : list) {
            items.put(b.getId(), b);
            if (b.getId() >= nextId) {
                nextId = b.getId() + 1;
            }
        }
    }

    /**
     * Возвращает список всех партий для сохранения в файл.
     *
     * @return список партий
     */
    public List<ReagentBatch> getList() {
        return new ArrayList<>(items.values());
    }
}