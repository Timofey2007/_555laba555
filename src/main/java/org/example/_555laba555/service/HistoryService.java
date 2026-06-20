package org.example._555laba555.service;

import org.example._555laba555.dataBase.HistoryRepository;
import org.example._555laba555.domain.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryService {
    private static final String SEP = "\t"; // штукенция которая будет отделять каждый из элементов
    private final HistoryRepository repository;
    private final ReagentService reagentService;
    private final BatchService batchService;
    private final MoveService moveService;

    public HistoryService(HistoryRepository repository,
                          ReagentService reagentService,
                          BatchService batchService,
                          MoveService moveService) {
        this.repository = repository;
        this.reagentService = reagentService;
        this.batchService = batchService;
        this.moveService = moveService;
    }

    public void pushHistory(String type, Object deletedObject, long objectId, long deletedBy) {
        HistoryRecord record = new HistoryRecord();
        record.setObjectType(type);
        record.setObjectId(objectId);
        record.setObjectData(serialize(deletedObject));
        record.setDeletedBy(deletedBy);
        record.setDeletedAt(LocalDateTime.now());
        repository.save(record);
    }

    public List<HistoryRecord> getAll() {
        return repository.findAll();
    }

    public boolean restore(long historyId, long currentUserId) {
        HistoryRecord record = repository.findById(historyId);
        if (record == null) return false;

        try {
            Object obj = deserialize(record.getObjectType(), record.getObjectData());
            if (obj == null) return false;

            long ownerId = getOwnerId(obj);
            if (ownerId != currentUserId) return false;

            switch (record.getObjectType()) {
                case "reagent":
                    reagentService.add((Reagent) obj);
                    break;
                case "batch":
                    batchService.add((ReagentBatch) obj);
                    break;
                case "move":
                    moveService.add((StockMove) obj, Double.MAX_VALUE);
                    break;
                default:
                    return false;
            }
            repository.delete(historyId);
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка восстановления: " + e.getMessage());
            return false;
        }
    }

    private long getOwnerId(Object obj) {
        if (obj instanceof Reagent r) return r.getOwnerId();
        if (obj instanceof ReagentBatch b) return b.getOwnerID();
        if (obj instanceof StockMove m) return m.getOwnerID();
        return 0;
    }

    private String serialize(Object obj) {
        if (obj instanceof Reagent r) {
            return String.join(SEP,
                    String.valueOf(r.getId()),
                    safe(r.getName()), safe(r.getFormula()), safe(r.getCas()),
                    safe(r.getHazardClass()),
                    String.valueOf(r.getOwnerId()), safe(r.getOwnerName()),
                    r.getCreatedAt() != null ? String.valueOf(r.getCreatedAt().toEpochMilli()) : "",
                    r.getUpdatedAt() != null ? String.valueOf(r.getUpdatedAt().toEpochMilli()) : ""
            );
        } else if (obj instanceof ReagentBatch b) {
            return String.join(SEP,
                    String.valueOf(b.getId()), String.valueOf(b.getReagentId()),
                    safe(b.getLabel()), String.valueOf(b.getQuantityCurrent()),
                    b.getUnit() != null ? b.getUnit().name() : "",
                    safe(b.getLocation()),
                    b.getExpiresAt() != null ? String.valueOf(b.getExpiresAt().toEpochMilli()) : "",
                    b.getStatus() != null ? b.getStatus().name() : "",
                    String.valueOf(b.getOwnerID()), safe(b.getOwnerUsername()),
                    b.getCreatedAt() != null ? String.valueOf(b.getCreatedAt().toEpochMilli()) : "",
                    b.getUpdatedAt() != null ? String.valueOf(b.getUpdatedAt().toEpochMilli()) : ""
            );
        } else if (obj instanceof StockMove m) {
            return String.join(SEP,
                    String.valueOf(m.getId()), String.valueOf(m.getBatchId()),
                    m.getType() != null ? m.getType().name() : "",
                    String.valueOf(m.getQuantity()),
                    m.getUnit() != null ? m.getUnit().name() : "",
                    safe(m.getReason()),
                    String.valueOf(m.getOwnerID()), safe(m.getOwnerUsername()),
                    m.getMovedAt() != null ? String.valueOf(m.getMovedAt().toEpochMilli()) : "",
                    m.getCreatedAt() != null ? String.valueOf(m.getCreatedAt().toEpochMilli()) : ""
            );
        }
        return "";
    }

    private Object deserialize(String type, String data) {
        if (data == null || data.isEmpty()) return null;
        String[] p = data.split(SEP, -1);
        try {
            switch (type) {
                case "reagent": return deserReagent(p);
                case "batch":   return deserBatch(p);
                case "move":    return deserMove(p);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private Reagent deserReagent(String[] p) {
        Reagent r = new Reagent();
        r.setId(Long.parseLong(p[0]));
        r.setName(p[1]);
        r.setFormula(emptyToNull(p[2]));
        r.setCas(emptyToNull(p[3]));
        r.setHazardClass(emptyToNull(p[4]));
        r.setOwnerId(Long.parseLong(p[5]));
        r.setOwnerName(emptyToNull(p[6]));
        if (!p[7].isEmpty()) r.setCreatedAt(Instant.ofEpochMilli(Long.parseLong(p[7])));
        if (p.length > 8 && !p[8].isEmpty()) r.setUpdatedAt(Instant.ofEpochMilli(Long.parseLong(p[8])));
        return r;
    }

    private ReagentBatch deserBatch(String[] p) {
        ReagentBatch b = new ReagentBatch();
        b.setId(Long.parseLong(p[0]));
        b.setReagentId(Long.parseLong(p[1]));
        b.setLabel(p[2]);
        b.setQuantityCurrent(Double.parseDouble(p[3]));
        if (!p[4].isEmpty()) b.setUnit(BatchUnit.valueOf(p[4]));
        b.setLocation(p[5]);
        if (!p[6].isEmpty()) b.setExpiresAt(Instant.ofEpochMilli(Long.parseLong(p[6])));
        if (!p[7].isEmpty()) b.setStatus(BatchStatus.valueOf(p[7]));
        b.setOwnerID(Long.parseLong(p[8]));
        b.setOwnerUsername(p[9]);
        if (p.length > 10 && !p[10].isEmpty()) b.setCreatedAt(Instant.ofEpochMilli(Long.parseLong(p[10])));
        if (p.length > 11 && !p[11].isEmpty()) b.setUpdatedAt(Instant.ofEpochMilli(Long.parseLong(p[11])));
        return b;
    }

    private StockMove deserMove(String[] p) {
        StockMove m = new StockMove();
        m.setId(Long.parseLong(p[0]));
        m.setBatchId(Long.parseLong(p[1]));
        if (!p[2].isEmpty()) m.setType(StockMoveType.valueOf(p[2]));
        m.setQuantity(Double.parseDouble(p[3]));
        if (!p[4].isEmpty()) m.setUnit(BatchUnit.valueOf(p[4]));
        m.setReason(emptyToNull(p[5]));
        m.setOwnerID(Long.parseLong(p[6]));
        m.setOwnerUsername(p[7]);
        if (!p[8].isEmpty()) m.setMovedAt(Instant.ofEpochMilli(Long.parseLong(p[8])));
        if (p.length > 9 && !p[9].isEmpty()) m.setCreatedAt(Instant.ofEpochMilli(Long.parseLong(p[9])));
        return m;
    }

    private String safe(String s) { return s == null ? "" : s; }
    private String emptyToNull(String s) { return (s == null || s.isEmpty()) ? null : s; }
}