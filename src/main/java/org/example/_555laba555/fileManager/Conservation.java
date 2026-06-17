package org.example._555laba555.fileManager;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.example._555laba555.domain.*;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.storage.Storage;

import java.io.*;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Conservation implements Storage {

    private final String csvFile;// = "records.csv";// ну условно


    public Conservation(String csvFile) { //для load
        this.csvFile = csvFile;
    }

    // конструктор по умолчанию
    public Conservation() { // для save/load

        this("records.csv");
    }

    public void save(ServiceManager service) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(this.csvFile))) {

            //REAGENTS
            writer.writeNext(new String[]{"[REAGENTS]"});
            for (Reagent r : service.getReagentService().getAll()) {
                writer.writeNext(new String[]{
                        "REAGENT",
                        String.valueOf(r.getId()), // принимает long и возвращает строку, удобный системный метод парсинга для методов сохранения по сути
                        r.getName() != null ? r.getName() : "", // здесь поле у нас может не заполняться создателем и поэтому делаем с помощью тернарного оператора так, чтобы можно было использовать поле но оставить его пустым по сути
                        r.getFormula() != null ? r.getFormula() : "",
                        r.getCas() != null ? r.getCas() : "",
                        r.getHazardClass() != null ? r.getHazardClass() : "",
                        r.getOwnerName() != null ? r.getOwnerName() : "",
                        r.getCreatedAt() != null ? r.getCreatedAt().toString() : "",
                        r.getUpdatedAt() != null ? r.getUpdatedAt().toString() : ""
                });
            }

            //BATCHES
            writer.writeNext(new String[]{"[BATCHES]"});
            for (ReagentBatch b : service.getBatchService().getAll()) {
                writer.writeNext(new String[]{
                        "BATCH",
                        String.valueOf(b.getId()),
                        String.valueOf(b.getReagentId()),
                        b.getLabel() != null ? b.getLabel() : "",
                        String.valueOf(b.getQuantityCurrent()),
                        b.getUnit() != null ? b.getUnit().name() : "",
                        b.getLocation() != null ? b.getLocation() : "",
                        b.getExpiresAt() != null ? b.getExpiresAt().toString() : "",
                        b.getStatus() != null ? b.getStatus().name() : "",
                        b.getOwnerUsername() != null ? b.getOwnerUsername() : "",
                        b.getCreatedAt() != null ? b.getCreatedAt().toString() : "",
                        b.getUpdatedAt() != null ? b.getUpdatedAt().toString() : ""
                });
            }

            //MOVES
            writer.writeNext(new String[]{"[MOVES]"});
            for (StockMove m : service.getMoveService().getAll()) {
                writer.writeNext(new String[]{
                        "MOVE",
                        String.valueOf(m.getId()),
                        String.valueOf(m.getBatchId()),
                        m.getType() != null ? m.getType().name() : "",
                        String.valueOf(m.getQuantity()),
                        m.getUnit() != null ? m.getUnit().name() : "",
                        m.getReason() != null ? m.getReason() : "",
                        m.getOwnerUsername() != null ? m.getOwnerUsername() : "",
                        m.getMovedAt() != null ? m.getMovedAt().toString() : "",
                        m.getCreatedAt() != null ? m.getCreatedAt().toString() : ""
                });
            }

        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }

    }

    public void load(ServiceManager serviceManager) {
        File file = new File(csvFile);

        if (!file.exists()) {
            System.out.println("программа не нашла файла, начинаем заново");
            return;
        }
        if (file.length() == 0) {
            System.out.println("файл пустой, начинаем с пустыми данными");
            return;
        }
        List<Reagent> tempReagents = new ArrayList<>();
        List<ReagentBatch> tempBatches = new ArrayList<>();
        List<StockMove> tempMoves = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {

            String[] line;
            String currentSection = ""; // что-то что будет заполняемо

            while ((line = reader.readNext()) != null) {
                if (line.length == 0) continue; // переход к следующей итерации цикла

                if (line[0].startsWith("[")) {
                    currentSection = line[0];
                    continue;
                }

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



            serviceManager.getReagentService().loadFromList(tempReagents);
            serviceManager.getBatchService().loadFromList(tempBatches);
            serviceManager.getMoveService().loadFromList(tempMoves);
            System.out.println("Загружено " + tempReagents.size() + " реактивов, " +
                    tempBatches.size() + " партий, " + tempMoves.size() + " движений");

        } catch (Exception e) {
            System.err.println("Ошибка загрузки: " + e.getMessage());
        }
    }


    private Reagent parseReagent(String[] data) {
        try {
            Reagent r = new Reagent(); // создаем пустой реагент без всего и начинаем заполнять полями которые есть в csv файле
            r.setId(Long.parseLong(data[1]));
            r.setName(data[2]);
            r.setFormula(emptyToNull(data[3]));
            r.setCas(emptyToNull(data[4]));
            r.setHazardClass(emptyToNull(data[5]));
            r.setOwnerName(emptyToNull(data[6]));
            // тут у нас идут поля с приколами поэтому делаем внутреннюю валидацию еще

            if (data.length > 7 && data[7] != null && !data[7].isEmpty()) {
                try {
                    r.setCreatedAt(Instant.parse(data[7]));
                } catch (DateTimeParseException ignored) {}
            }

            if (data.length > 8 && data[8] != null && !data[8].isEmpty()) {
                try {
                    r.setUpdatedAt(Instant.parse(data[8]));
                } catch (DateTimeParseException ignored) {}
            }

            return r;
        } catch (Exception e) {
            System.err.println("Ошибка парсинга реактива: " + e.getMessage());
            return null;
        }
    }


    private ReagentBatch parseBatch(String[] data) {
        try {
            ReagentBatch b = new ReagentBatch();// создаем пустую партию без всего и начинаем заполнять полями которые есть в csv файле
            b.setId(Long.parseLong(data[1]));
            b.setReagentId(Long.parseLong(data[2]));
            b.setLabel(data[3]);
            b.setQuantityCurrent(Double.parseDouble(data[4]));
            b.setUnit(BatchUnit.valueOf(data[5]));
            b.setLocation(data[6]);
            // тут у нас тоже идут поля с приколами поэтому делаем внутреннюю валидацию еще
            if (data.length > 7 && data[7] != null && !data[7].isEmpty()) {
                try {
                    b.setExpiresAt(Instant.parse(data[7]));
                } catch (DateTimeParseException ignored) {}
            }

            if (data.length > 8 && data[8] != null && !data[8].isEmpty()) {
                b.setStatus(BatchStatus.valueOf(data[8]));
            }

            if (data.length > 9 && data[9] != null && !data[9].isEmpty()) {
                b.setOwnerUsername(data[9]);
            }

            if (data.length > 10 && data[10] != null && !data[10].isEmpty()) {
                try {
                    b.setCreatedAt(Instant.parse(data[10]));
                } catch (DateTimeParseException ignored) {}
            }

            if (data.length > 11 && data[11] != null && !data[11].isEmpty()) {
                try {
                    b.setUpdatedAt(Instant.parse(data[11]));
                } catch (DateTimeParseException ignored) {}
            }

            return b;
        } catch (Exception e) {
            System.err.println("Ошибка парсинга партии: " + e.getMessage());
            return null;
        }
    }


    private StockMove parseMove(String[] data) {
        try {
            StockMove m = new StockMove(); // создаем пустое передвижение без всего и начинаем заполнять полями которые есть в csv файле
            m.setId(Long.parseLong(data[1]));
            m.setBatchId(Long.parseLong(data[2]));
            m.setType(StockMoveType.valueOf(data[3]));
            m.setQuantity(Double.parseDouble(data[4]));
            m.setUnit(BatchUnit.valueOf(data[5]));
            m.setReason(emptyToNull(data[6]));
            m.setOwnerUsername(emptyToNull(data[7]));
            // и тут у нас также идут поля с приколами поэтому снова делаем внутреннюю валидацию еще

            if (data.length > 8 && data[8] != null && !data[8].isEmpty()) {
                try {
                    m.setMovedAt(Instant.parse(data[8]));
                } catch (DateTimeParseException ignored) {}
            }

            if (data.length > 9 && data[9] != null && !data[9].isEmpty()) {
                try {
                    m.setCreatedAt(Instant.parse(data[9]));
                } catch (DateTimeParseException ignored) {}
            }

            return m;
        } catch (Exception e) {
            System.err.println("Ошибка парсинга движения: " + e.getMessage());
            return null;
        }
    }


    private String emptyToNull(String s) { // по сути проверяет не передали пустую строку(использовать буду при парсинге)
        return (s == null || s.isEmpty()) ? null : s;
    }
    public String getCsvFile() {
        return csvFile;
    }
    
}