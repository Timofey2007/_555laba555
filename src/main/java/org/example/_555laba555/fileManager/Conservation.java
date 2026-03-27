package org.example._555laba555.fileManager;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.example._555laba555.domain.*;
import org.example._555laba555.service.ServiceManager;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для сохранения и загрузки данных в CSV файл.
 * Использует библиотеку OpenCSV для работы с CSV форматом.
 * Данные сохраняются в три секции: REAGENTS, BATCHES, MOVES.
 *
 */
public class Conservation {

    /** Путь к файлу для сохранения данных */
    private final String dataFile;

    /**
     * Конструктор класса.
     *
     * @param dataFile путь к файлу, куда будут сохраняться данные
     */
    public Conservation(String dataFile) {
        this.dataFile = dataFile;
    }

    /**
     * Сохраняет все данные из сервисов в CSV файл.
     * <p>
     * Сначала сохраняются реактивы, затем партии, затем движения.
     * Каждая секция отделена заголовком [REAGENTS], [BATCHES], [MOVES].
     * </p>
     *
     * @param services менеджер сервисов, содержащий все данные
     * @throws StorageException если не удалось записать в файл
     */
    public void save(ServiceManager services) throws StorageException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(dataFile))) {

            // Секция реактивов
            writer.writeNext(new String[]{"[REAGENTS]"});
            for (Reagent r : services.getReagentService().getAll()) {
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

            // Секция партий
            writer.writeNext(new String[]{"[BATCHES]"});
            for (ReagentBatch b : services.getBatchService().getAll()) {
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

            // Секция движений
            writer.writeNext(new String[]{"[MOVES]"});
            for (StockMove m : services.getMoveService().getAll()) {
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
            throw new StorageException("Не удалось сохранить файл: " + e.getMessage(), e);
        }
    }

    /**
     * Загружает данные из CSV файла в сервисы.
     * <p>
     * Сначала данные читаются во временные списки, затем проходят валидацию,
     * и только после этого заменяют данные в сервисах.
     * Если файл не найден, загрузка пропускается.
     * </p>
     *
     * @param services менеджер сервисов для загрузки данных
     * @throws StorageException если файл поврежден или данные не проходят валидацию
     */
    public void load(ServiceManager services) throws StorageException {
        File file = new File(dataFile);
        if (!file.exists()) {
            System.out.println("Файл не найден, начинаем с пустыми данными");
            return;
        }

        List<Reagent> tempReagents = new ArrayList<>();
        List<ReagentBatch> tempBatches = new ArrayList<>();
        List<StockMove> tempMoves = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(dataFile))) {
            String[] line;
            String currentSection = "";

            while ((line = reader.readNext()) != null) {
                if (line.length == 0) continue;

                // Определяем секцию по заголовку
                if (line[0].startsWith("[")) {
                    currentSection = line[0];
                    continue;
                }

                // Парсим данные в зависимости от секции
                switch (currentSection) {
                    case "[REAGENTS]":
                        if (line[0].equals("REAGENT")) {
                            Reagent r = parseReagent(line);
                            if (r != null) tempReagents.add(r);
                        }
                        break;
                    case "[BATCHES]":
                        if (line[0].equals("BATCH")) {
                            ReagentBatch b = parseBatch(line);
                            if (b != null) tempBatches.add(b);
                        }
                        break;
                    case "[MOVES]":
                        if (line[0].equals("MOVE")) {
                            StockMove m = parseMove(line);
                            if (m != null) tempMoves.add(m);
                        }
                        break;
                }
            }

            // Проверяем целостность данных
            validateData(tempReagents, tempBatches, tempMoves);

            // Загружаем данные в сервисы
            services.getReagentService().loadFromList(tempReagents);
            services.getBatchService().loadFromList(tempBatches);
            services.getMoveService().loadFromList(tempMoves);

            System.out.println("Загружено: " + tempReagents.size() + " реактивов, " +
                    tempBatches.size() + " партий, " + tempMoves.size() + " движений");

        } catch (IOException e) {
            throw new StorageException("Ошибка чтения файла: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new StorageException("Ошибка при загрузке: " + e.getMessage(), e);
        }
    }

    /**
     * Преобразует строку CSV в объект Reagent.
     *
     * @param data массив строк из CSV (одна запись)
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
            System.err.println("Ошибка парсинга реактива: " + e.getMessage());
            return null;
        }
    }

    /**
     * Преобразует строку CSV в объект ReagentBatch.
     *
     * @param data массив строк из CSV (одна запись)
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
            System.err.println("Ошибка парсинга партии: " + e.getMessage());
            return null;
        }
    }

    /**
     * Преобразует строку CSV в объект StockMove.
     *
     * @param data массив строк из CSV (одна запись)
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
            System.err.println("Ошибка парсинга движения: " + e.getMessage());
            return null;
        }
    }

    /**
     * Проверяет целостность загруженных данных.
     * <p>
     * Проверяет:
     * <ul>
     *   <li>Каждая партия ссылается на существующий реактив</li>
     *   <li>Каждое движение ссылается на существующую партию</li>
     * </ul>
     * </p>
     *
     * @param reagents список загруженных реактивов
     * @param batches список загруженных партий
     * @param moves список загруженных движений
     * @throws StorageException если найдена ссылка на несуществующий объект
     */
    private void validateData(List<Reagent> reagents, List<ReagentBatch> batches,
                              List<StockMove> moves) throws StorageException {

        // Проверка ссылок партий на реактивы
        for (ReagentBatch b : batches) {
            boolean found = false;
            for (Reagent r : reagents) {
                if (r.getId() == b.getReagentId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new StorageException("Партия " + b.getId() +
                        " ссылается на несуществующий реактив " + b.getReagentId());
            }
        }

        // Проверка ссылок движений на партии
        for (StockMove m : moves) {
            boolean found = false;
            for (ReagentBatch b : batches) {
                if (b.getId() == m.getBatchId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new StorageException("Движение " + m.getId() +
                        " ссылается на несуществующую партию " + m.getBatchId());
            }
        }
    }

    /**
     * Преобразует объект в строку, заменяя null на пустую строку.
     *
     * @param obj любой объект
     * @return строковое представление объекта или пустая строка
     */
    private String valueOrEmpty(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * Преобразует пустую строку в null.
     *
     * @param s строка
     * @return null если строка пустая, иначе исходную строку
     */
    private String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}