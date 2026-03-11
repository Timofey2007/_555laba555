package org.example._555laba555.base;

import com.opencsv.*;
import java.io.*;
import java.util.*;

/**
 * Этот класс сохраняет и загружает данные в файл и из него, когда программа запускается заново
 */
public class ProgramState {

    private static final String DATA_FILE = "lab5_data.csv";

    private List<Reagent> reagents = new ArrayList<>();
    private List<ReagentBatch> batches = new ArrayList<>();
    private List<StockMove> moves = new ArrayList<>();

    public ProgramState() {
        loadState();
    }

    // СОХРАНЕНИЕ
    public void saveState() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(DATA_FILE))) {

            writer.writeNext(new String[]{"[REAGENTS]"});
            for (Reagent r : reagents) {
                writer.writeNext(new String[]{
                        "REAGENT",
                        String.valueOf(r.getId()),
                        r.getName(),
                        r.getFormula() != null ? r.getFormula() : "",
                        r.getCas() != null ? r.getCas() : "",
                        r.getHazardClass() != null ? r.getHazardClass() : ""
                });
            }

            // Сохраняем партии
            writer.writeNext(new String[]{"[BATCHES]"});
            for (ReagentBatch b : batches) {
                writer.writeNext(new String[]{
                        "BATCH",
                        String.valueOf(b.getId()),
                        String.valueOf(b.getReagentId()),
                        b.getLabel(),
                        String.valueOf(b.getQuantityCurrent()),
                        b.getUnit().name(),
                        b.getLocation()
                });
            }

            // Сохраняем передвижения
            writer.writeNext(new String[]{"[MOVES]"});
            for (StockMove m : moves) {
                writer.writeNext(new String[]{
                        "MOVE",
                        String.valueOf(m.getId()),
                        String.valueOf(m.getBatchId()),
                        m.getType().name(),
                        String.valueOf(m.getQuantity()),
                        m.getUnit().name(),
                        m.getReason() != null ? m.getReason() : ""
                });
            }

            System.out.println("Данные сохранены в файл: " + DATA_FILE);

        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    // ЗАГРУЗКА
    public void loadState() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Файл не найден, начинаем с пустыми данными");
            return;
        }

        try (CSVReader reader = new CSVReader(new FileReader(DATA_FILE))) {
            String[] line;
            String currentSection = "";

            while ((line = reader.readNext()) != null) {
                if (line.length == 0) continue;

                // Определяем секцию
                switch (line[0]) {
                    case "[REAGENTS]" -> {
                        currentSection = "REAGENTS";
                        continue;
                    }
                    case "[BATCHES]" -> {
                        currentSection = "BATCHES";
                        continue;
                    }
                    case "[MOVES]" -> {
                        currentSection = "MOVES";
                        continue;
                    }
                }

                // Загружаем данные
                switch (currentSection) {
                    case "REAGENTS":
                        if (line[0].equals("REAGENT")) loadReagent(line);
                        break;
                    case "BATCHES":
                        if (line[0].equals("BATCH")) loadBatch(line);
                        break;
                    case "MOVES":
                        if (line[0].equals("MOVE")) loadMove(line);
                        break;
                }
            }

            System.out.println("Данные загружены из файла:");
            System.out.println("Реактивов: " + reagents.size());
            System.out.println("Партий: " + batches.size());
            System.out.println("Передвижений: " + moves.size());

        } catch (Exception e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
        }
    }

    // Вспомогательные методы загрузки
    private void loadReagent(String[] data) {
        try {
            Reagent r = new Reagent();
            r.setId(Long.parseLong(data[1]));
            r.setName(data[2]);
            r.setFormula(emptyToNull(data[3]));
            r.setCas(emptyToNull(data[4]));
            r.setHazardClass(emptyToNull(data[5]));
            reagents.add(r);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки реактива: " + e.getMessage());
        }
    }

    private void loadBatch(String[] data) {
        try {
            ReagentBatch b = new ReagentBatch();
            b.setId(Long.parseLong(data[1]));
            b.setReagentId(Long.parseLong(data[2]));
            b.setLabel(data[3]);
            b.setQuantityCurrent(Double.parseDouble(data[4]));
            b.setUnit(BatchUnit.valueOf(data[5]));
            b.setLocation(data[6]);
            batches.add(b);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки партии: " + e.getMessage());
        }
    }

    private void loadMove(String[] data) {
        try {
            StockMove m = new StockMove();
            m.setId(Long.parseLong(data[1]));
            m.setBatchId(Long.parseLong(data[2]));
            m.setType(StockMoveType.valueOf(data[3]));
            m.setQuantity(Double.parseDouble(data[4]));
            m.setUnit(BatchUnit.valueOf(data[5]));
            m.setReason(emptyToNull(data[6]));
            moves.add(m);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки передвижения: " + e.getMessage());
        }
    }

    private String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }

    // Методы для работы с данными
    public void addReagent(Reagent r) {
        reagents.add(r);
        saveState();
    }

    public void addBatch(ReagentBatch b) {
        batches.add(b);
        saveState();
    }

    public void addMove(StockMove m) {
        moves.add(m);
        saveState();
    }

    public void removeReagent(long id) {
        reagents.removeIf(r -> r.getId() == id);
        saveState();
    }
    public void clearAllData() {
        reagents.clear();
        batches.clear();
        moves.clear();
        saveState();
        System.out.println( DATA_FILE + " очищен");
    }

    // Геттеры
    public List<Reagent> getReagents() { return reagents; }
    public List<ReagentBatch> getBatches() { return batches; }
    public List<StockMove> getMoves() { return moves; }
}