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
 * Хранит данные в HashMap
 */
public class ReagentService {
    private Map<Long, Reagent> items = new HashMap<>();

    private long nextId = 1;

    /**
     * Добавляет новый реактив в коллекцию.
     * Генерирует ID, устанавливает даты создания и обновления,
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
     * Возвращает список всех реактивов.
     */
    public List<Reagent> getAll() {
        return new ArrayList<>(items.values());
    }

    /**
     * Ищет реактивы по названию
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

    public boolean exist(long id) {
        return items.containsKey(id);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Загружает реактивы из списка (используется при чтении файла).
     * Очищает текущую коллекцию и заполняет новой.
     *
     * @param list список реактивов для загрузки
     */
    public void loadFromList(List<Reagent> list) {
        items.clear();  // очищаем старые данные
        for (Reagent r : list) {
            items.put(r.getId(), r);
            // обновляем счетчик ID, чтобы новые объекты не пересекались
            if (r.getId() >= nextId) {
                nextId = r.getId() + 1;
            }
        }
    }
    public void update(Reagent reagent) {
        if (!items.containsKey(reagent.getId())) {
            throw new ValidationException("Реактив не найден");
        }
        reagent.setUpdatedAt(Instant.now());
        ReagentValidator.validate(reagent);
        items.put(reagent.getId(), reagent);
    }

    public void remove(long id) {
        if (!items.containsKey(id)) {
            throw new ValidationException("Реактив не найден");
        }
        items.remove(id);
    }
}