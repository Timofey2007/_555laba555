package org.example._555laba555.storage;

import org.example._555laba555.domain.*;
import org.example._555laba555.service.ServiceManager;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация хранилища данных в CSV файле.
 * Отвечает за сохранение и загрузку всех сущностей (реактивы, партии, движения).
 * Использует библиотеку OpenCSV для работы с CSV форматом.
 *
 * @author Студент
 * @version 1.0
 */
public class FileStorage {
    /** Имя файла для хранения данных */
    private String filename;

    /**
     * Создает хранилище с файлом по умолчанию "lab5_data.csv".
     */
    public FileStorage() {
        this.filename = "lab5_data.csv";
    }

    /**
     * Создает хранилище с указанным именем файла.
     *
     * @param filename имя файла для сохранения данных
     */
    public FileStorage(String filename) {
        this.filename = filename;
    }

    /**
     * Сохраняет все данные из сервисов в CSV файл.
     * Данные сохраняются по секциям: [REAGENTS], [BATCHES], [MOVES].
     *
     * @param services менеджер сервисов с данными
     * @throws StorageException если произошла ошибка при записи файла
     */
    public void save(ServiceManager services) throws StorageException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {

            writer.writeNext(new String[]{"[REAGENTS]"});
            for (Reagent r : services.getReagentService().getList()) {
                writer.writeNext(new String[]{
                        "REAGENT",
                        String.valueOf(r.getId()),
                        valueOrEmpty(r.getName()),
                        valueOrEmpty(r.getFormula()),
                        valueOrEmpty(r.getCas()),
                        valueOrEmpty(r.getHazardClass()),
                        valueOrEmpty(r.getOwnerUsername()),
                        valueOrEmpty(r.getCreatedAt()),
                        valueOrEmpty(r.getUpdatedAt())
                });
            }

            writer.writeNext(new String[]{"[BATCHES]"});
            for (ReagentBatch b : services.getBatchService().getList()) {
                writer.writeNext(new String[]{
                        "BATCH",
                        String.valueOf(b.getId()),
                        String.valueOf(b.getReagentId()),
                        valueOrEmpty(b.getLabel()),
                        String.valueOf(b.getQuantityCurrent()),
                        b.getUnit() != null ? b.getUnit().name() : "",
                        valueOrEmpty(b.getLocation()),
                        valueOrEmpty(b.getExpiresAt()),
                        b.getStatus() != null ? b.getStatus().name() : "",
                        valueOrEmpty(b.getOwnerUsername()),
                        valueOrEmpty(b.getCreatedAt()),
                        valueOrEmpty(b.getUpdatedAt())
                });
            }

            writer.writeNext(new String[]{"[MOVES]"});
            for (StockMove m : services.getMoveService().getList()) {
                writer.writeNext(new String[]{
                        "MOVE",
                        String.valueOf(m.getId()),
                        String.valueOf(m.getBatchId()),
                        m.getType() != null ? m.getType().name() : "",
                        String.valueOf(m.getQuantity()),
                        m.getUnit() != null ? m.getUnit().name() : "",
                        valueOrEmpty(m.getReason()),
                        valueOrEmpty(m.getOwnerUsername()),
                        valueOrEmpty(m.getMovedAt()),
                        valueOrEmpty(m.getCreatedAt())
                });
            }

        } catch (IOException e) {
            throw new StorageException("Не удалось сохранить файл: " + e.getMessage());
        }
    }

    /**
     * Загружает данные из CSV файла в сервисы.
     * Если файл не существует, загрузка пропускается.
     *
     * @param services менеджер сервисов для загрузки данных
     * @throws StorageException если произошла ошибка при чтении файла
     */
    public void load(ServiceManager services) throws StorageException {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }

        List<Reagent> reagents = new ArrayList<>();
        List<ReagentBatch> batches = new ArrayList<>();
        List<StockMove> moves = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            String[] line;
            String section = "";

            while ((line = reader.readNext()) != null) {
                if (line.length == 0) continue;

                if (line[0].startsWith("[")) {
                    section = line[0];
                    continue;
                }

                switch (section) {
                    case "[REAGENTS]":
                        Reagent r = parseReagent(line);
                        if (r != null) reagents.add(r);
                        break;
                    case "[BATCHES]":
                        ReagentBatch b = parseBatch(line);
                        if (b != null) batches.add(b);
                        break;
                    case "[MOVES]":
                        StockMove m = parseMove(line);
                        if (m != null) moves.add(m);
                        break;
                }
            }

            services.getReagentService().loadFromList(reagents);
            services.getBatchService().loadFromList(batches);
            services.getMoveService().loadFromList(moves);

        } catch (Exception e) {
            throw new StorageException("Ошибка чтения файла: " + e.getMessage());
        }
    }

    /**
     * Преобразует строку CSV в объект Reagent.
     *
     * @param data массив строк из CSV
     * @return объект Reagent или null при ошибке
     */
    private Reagent parseReagent(String[] data) {
        try {
            Reagent r = new Reagent();
            r.setId(Long.parseLong(data[1]));
            r.setName(data[2]);
            r.setFormula(emptyToNull(data[3]));
            r.setCas(emptyToNull(data[4]));
            r.setHazardClass(emptyToNull(data[5]));
            r.setOwnerUsername(emptyToNull(data[6]));
            if (data.length > 7 && !data[7].isEmpty()) {
                r.setCreatedAt(Instant.parse(data[7]));
            }
            if (data.length > 8 && !data[8].isEmpty()) {
                r.setUpdatedAt(Instant.parse(data[8]));
            }
            return r;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Преобразует строку CSV в объект ReagentBatch.
     *
     * @param data массив строк из CSV
     * @return объект ReagentBatch или null при ошибке
     */
    private ReagentBatch parseBatch(String[] data) {
        try {
            ReagentBatch b = new ReagentBatch();
            b.setId(Long.parseLong(data[1]));
            b.setReagentId(Long.parseLong(data[2]));
            b.setLabel(data[3]);
            b.setQuantityCurrent(Double.parseDouble(data[4]));
            b.setUnit(BatchUnit.valueOf(data[5]));
            b.setLocation(data[6]);
            if (data.length > 7 && !data[7].isEmpty()) {
                b.setExpiresAt(Instant.parse(data[7]));
            }
            if (data.length > 8 && !data[8].isEmpty()) {
                b.setStatus(BatchStatus.valueOf(data[8]));
            }
            if (data.length > 9 && !data[9].isEmpty()) {
                b.setOwnerUsername(data[9]);
            }
            if (data.length > 10 && !data[10].isEmpty()) {
                b.setCreatedAt(Instant.parse(data[10]));
            }
            if (data.length > 11 && !data[11].isEmpty()) {
                b.setUpdatedAt(Instant.parse(data[11]));
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Преобразует строку CSV в объект StockMove.
     *
     * @param data массив строк из CSV
     * @return объект StockMove или null при ошибке
     */
    private StockMove parseMove(String[] data) {
        try {
            StockMove m = new StockMove();
            m.setId(Long.parseLong(data[1]));
            m.setBatchId(Long.parseLong(data[2]));
            m.setType(StockMoveType.valueOf(data[3]));
            m.setQuantity(Double.parseDouble(data[4]));
            m.setUnit(BatchUnit.valueOf(data[5]));
            m.setReason(emptyToNull(data[6]));
            if (data.length > 7 && !data[7].isEmpty()) {
                m.setOwnerUsername(data[7]);
            }
            if (data.length > 8 && !data[8].isEmpty()) {
                m.setMovedAt(Instant.parse(data[8]));
            }
            if (data.length > 9 && !data[9].isEmpty()) {
                m.setCreatedAt(Instant.parse(data[9]));
            }
            return m;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Преобразует объект в строку для CSV, заменяя null на пустую строку.
     *
     * @param obj любой объект
     * @return строковое представление или пустая строка
     */
    private String valueOrEmpty(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * Преобразует пустую строку в null.
     *
     * @param s строка
     * @return null если строка пустая или null, иначе исходную строку
     */
    private String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}