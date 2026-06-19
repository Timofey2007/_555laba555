package org.example._555laba555.service;

import org.example._555laba555.domain.Reagent;
import org.example._555laba555.dataBase.ReagentRepository;
import org.example._555laba555.validation.ReagentValidator;
import org.example._555laba555.validation.ValidationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReagentService {
    private final Map<Long, Reagent> items = new HashMap<>();
    private final ReagentRepository reagentRepository;

    public ReagentService(ReagentRepository reagentRepository) {
        this.reagentRepository = reagentRepository;
    }

    public Reagent add(Reagent reagent) {
        ReagentValidator.validate(reagent);
        reagent.setCreatedAt(Instant.now());
        reagent.setUpdatedAt(Instant.now());
        long generatedId = reagentRepository.save(reagent);
        reagent.setId(generatedId);
        items.put(generatedId, reagent);
        return reagent;
    }

    public void loadAll() {
        try {
            items.clear();
            List<Reagent> fromDb = reagentRepository.findAll();
            for (Reagent r : fromDb) {
                items.put(r.getId(), r);
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки реактивов: " + e.getMessage());
        }
    }

    public List<Reagent> getAll() {
        return new ArrayList<>(items.values());
    }

    // ✅ ДОБАВЛЕН НЕДОСТАЮЩИЙ МЕТОД
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

    public Reagent getById(long id) {
        return items.get(id);
    }

    public Reagent getReagentById(long id) {
        return items.get(id);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean exist(long id) {
        return items.containsKey(id);
    }

    public void remove(long id) {
        items.remove(id);
    }
}