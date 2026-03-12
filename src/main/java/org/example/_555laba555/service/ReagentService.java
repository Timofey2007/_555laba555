package org.example._555laba555.service;

import org.example._555laba555.domain.Reagent;
import org.example._555laba555.validation.ReagentValidator;
import org.example._555laba555.validation.ValidationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления коллекцией реактивов.
 * Отвечает за добавление, поиск, обновление и удаление реактивов.
 * Хранит данные в HashMap (вариант 2 из задания).
 *
 * @author Студент
 * @version 1.0
 */
public class ReagentService {
    /** Хранилище реактивов: ключ - ID, значение - объект Reagent */
    private Map<Long, Reagent> items = new HashMap<>();

    /** Следующий доступный ID для нового реактива */
    private long nextId = 1;

    /**
     * Добавляет новый реактив в коллекцию.
     * Генерирует ID, устанавливает даты создания и обновления,
     * выполняет валидацию перед сохранением.
     *
     * @param reagent реактив для добавления
     * @return добавленный реактив с установленным ID
     * @throws ValidationException если данные не прошли валидацию
     */
    public Reagent add(Reagent reagent) {
        reagent.setId(nextId++);
        reagent.setCreatedAt(Instant.now());
        reagent.setUpdatedAt(Instant.now());

        ReagentValidator.validate(reagent);

        items.put(reagent.getId(), reagent);
        return reagent;
    }

    /**
     * Возвращает реактив по его ID.
     *
     * @param id идентификатор реактива
     * @return реактив или null, если не найден
     */
    public Reagent getById(long id) {
        return items.get(id);
    }

    /**
     * Возвращает список всех реактивов.
     *
     * @return список всех реактивов
     */
    public List<Reagent> getAll() {
        return new ArrayList<>(items.values());
    }

    /**
     * Ищет реактивы по названию (регистронезависимо).
     *
     * @param text текст для поиска
     * @return список реактивов, название которых содержит искомый текст
     */
    public List<Reagent> searchByName(String text) {
        if (text == null || text.isEmpty()) {
            return getAll();
        }
        String lower = text.toLowerCase();
        List<Reagent> result = new ArrayList<>();
        for (Reagent r : items.values()) {
            if (r.getName().toLowerCase().contains(lower)) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Обновляет существующий реактив.
     *
     * @param reagent реактив с обновленными данными
     * @throws ValidationException если реактив не найден или данные невалидны
     */
    public void update(Reagent reagent) {
        if (!items.containsKey(reagent.getId())) {
            throw new ValidationException("Реактив с ID " + reagent.getId() + " не найден");
        }
        reagent.setUpdatedAt(Instant.now());
        ReagentValidator.validate(reagent);
        items.put(reagent.getId(), reagent);
    }

    /**
     * Удаляет реактив по ID.
     *
     * @param id идентификатор удаляемого реактива
     * @throws ValidationException если реактив не найден
     */
    public void remove(long id) {
        if (!items.containsKey(id)) {
            throw new ValidationException("Реактив с ID " + id + " не найден");
        }
        items.remove(id);
    }

    /**
     * Проверяет, существует ли реактив с указанным ID.
     *
     * @param id проверяемый идентификатор
     * @return true если реактив существует
     */
    public boolean exists(long id) {
        return items.containsKey(id);
    }

    /**
     * Проверяет, пуста ли коллекция реактивов.
     *
     * @return true если нет ни одного реактива
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Загружает реактивы из списка (используется при чтении из файла).
     * Очищает текущую коллекцию и заполняет новыми данными.
     *
     * @param list список реактивов для загрузки
     */
    public void loadFromList(List<Reagent> list) {
        items.clear();
        for (Reagent r : list) {
            items.put(r.getId(), r);
            if (r.getId() >= nextId) {
                nextId = r.getId() + 1;
            }
        }
    }

    /**
     * Возвращает список всех реактивов для сохранения в файл.
     *
     * @return список реактивов
     */
    public List<Reagent> getList() {
        return new ArrayList<>(items.values());
    }
}