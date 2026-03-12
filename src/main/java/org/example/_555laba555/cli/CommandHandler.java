package org.example._555laba555.cli;

import org.example._555laba555.domain.*;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.storage.FileStorage;
import org.example._555laba555.storage.StorageException;
import org.example._555laba555.validation.ValidationException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Основной обработчик команд пользователя.
 * Связывает ввод с консоли с бизнес-логикой сервисов.
 * Реализует все 10 команд из предметной области.
 *
 * @author Студент
 * @version 1.0
 */
public class CommandHandler {
    /** Менеджер сервисов для доступа к данным */
    private final ServiceManager services;

    /** Помощник для чтения ввода */
    private final InputHelper input;

    /** Хранилище для сохранения в файл */
    private final FileStorage storage;

    /** Флаг работы программы */
    private boolean running;

    /**
     * Создает обработчик команд и загружает данные из файла.
     */
    public CommandHandler() {
        this.services = new ServiceManager();
        this.input = new InputHelper(new BufferedReader(new InputStreamReader(System.in)));
        this.storage = new FileStorage();
        this.running = true;

        try {
            storage.load(services);
        } catch (StorageException e) {
            System.out.println("Предупреждение: " + e.getMessage());
        }
    }

    /**
     * Запускает основной цикл обработки команд.
     */
    public void run() {
        System.out.println("Программа учета реактивов");
        System.out.println("Введите help для списка команд");

        while (running) {
            try {
                System.out.print("> ");
                String line = input.readString("", false);
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String command = parts[0].toLowerCase();
                String args = parts.length > 1 ? parts[1] : "";

                switch (command) {
                    case "help":
                        showHelp();
                        break;
                    case "exit":
                        exit();
                        break;
                    case "reag_add":
                        addReagent();
                        break;
                    case "reag_list":
                        listReagents(args);
                        break;
                    case "batch_add":
                        addBatch();
                        break;
                    case "batch_list":
                        listBatches(args);
                        break;
                    case "batch_show":
                        showBatch(args);
                        break;
                    case "move_add":
                        addMove(args);
                        break;
                    case "move_list":
                        listMoves(args);
                        break;
                    case "batch_update":
                        updateBatch(args);
                        break;
                    case "batch_archive":
                        archiveBatch(args);
                        break;
                    case "stock_report":
                        stockReport(args);
                        break;
                    case "clear_data":
                        clearData();
                        break;
                    case "save":
                        save();
                        break;
                    default:
                        System.out.println("Неизвестная команда. Введите help");
                }
            } catch (ValidationException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (StorageException e) {
                System.out.println("Ошибка сохранения: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Показывает список доступных команд.
     */
    private void showHelp() {
        System.out.println("\nДоступные команды:");
        System.out.println("  reag_add                - создать новый реактив");
        System.out.println("  reag_list [--q ТЕКСТ]   - список реактивов (с поиском)");
        System.out.println("  batch_add               - добавить новую партию");
        System.out.println("  batch_list [ID]         - список партий (для реактива или все)");
        System.out.println("  batch_show <ID>         - показать карточку партии");
        System.out.println("  move_add <ID>           - добавить движение по партии");
        System.out.println("  move_list <ID> [--last N]- история движений");
        System.out.println("  batch_update <ID> поле=значение... - обновить партию");
        System.out.println("  batch_archive <ID>      - архивировать партию");
        System.out.println("  stock_report [--exp-before ДАТА] - отчет по складу");
        System.out.println("  clear_data              - удалить все данные");
        System.out.println("  save                    - сохранить в файл");
        System.out.println("  help                    - показать эту справку");
        System.out.println("  exit                    - выход из программы\n");
    }

    /**
     * Завершает работу программы с сохранением данных.
     */
    private void exit() throws StorageException {
        save();
        System.out.println("До свидания!");
        running = false;
    }

    /**
     * Сохраняет данные в файл.
     */
    private void save() throws StorageException {
        storage.save(services);
        System.out.println("Данные сохранены");
    }

    /**
     * Добавляет новый реактив.
     * Запрашивает все поля интерактивно.
     */
    private void addReagent() throws Exception {
        Reagent r = new Reagent();
        r.setName(input.readString("Название: ", true));
        r.setFormula(input.readOptional("Формула: "));
        r.setCas(input.readOptional("CAS: "));
        r.setHazardClass(input.readOptional("Класс опасности: "));
        r.setOwnerUsername(input.readString("Владелец: ", true));

        services.getReagentService().add(r);
        System.out.println("Реактив добавлен. ID: " + r.getId());
    }

    /**
     * Показывает список реактивов.
     *
     * @param args аргументы команды, может содержать --q для поиска
     */
    private void listReagents(String args) {
        String query = null;
        if (args.contains("--q")) {
            String[] parts = args.split("--q");
            if (parts.length > 1) {
                query = parts[1].trim();
            }
        }

        List<Reagent> list = query != null ?
                services.getReagentService().searchByName(query) :
                services.getReagentService().getAll();

        if (list.isEmpty()) {
            System.out.println("Реактивы не найдены");
            return;
        }

        System.out.printf("%-5s %-20s %-10s %-15s%n", "ID", "Название", "Формула", "CAS");
        System.out.println("------------------------------------------------");
        for (Reagent r : list) {
            System.out.printf("%-5d %-20s %-10s %-15s%n",
                    r.getId(),
                    truncate(r.getName(), 20),
                    truncate(r.getFormula(), 10),
                    truncate(r.getCas(), 15));
        }
    }

    /**
     * Добавляет новую партию.
     * Запрашивает все поля интерактивно.
     */
    private void addBatch() throws Exception {
        if (services.getReagentService().isEmpty()) {
            System.out.println("Сначала добавьте реактив");
            return;
        }

        long reagentId = input.readLong("ID реактива: ");
        if (!services.getReagentService().exists(reagentId)) {
            System.out.println("Реактив не найден");
            return;
        }

        ReagentBatch b = new ReagentBatch();
        b.setReagentId(reagentId);
        b.setLabel(input.readString("Метка (номер партии): ", true));
        b.setQuantityCurrent(input.readDouble("Количество: "));

        String unitStr = input.readString("Единицы (G или ML): ", true).toUpperCase();
        b.setUnit(BatchUnit.valueOf(unitStr));

        b.setLocation(input.readString("Местоположение: ", true));

        LocalDate expires = input.readDate("Срок годности (ГГГГ-ММ-ДД, пусто если нет): ");
        if (expires != null) {
            b.setExpiresAt(expires.atStartOfDay(ZoneOffset.UTC).toInstant());
        }

        int statusChoice = input.readChoice("Статус (1-ACTIVE, 2-ARCHIVED): ", 1, 2);
        b.setStatus(statusChoice == 1 ? BatchStatus.ACTIVE : BatchStatus.ARCHIVED);

        b.setOwnerUsername(input.readString("Владелец: ", true));

        services.getBatchService().add(b);
        System.out.println("Партия добавлена. ID: " + b.getId());
    }

    /**
     * Показывает список партий.
     *
     * @param args может содержать ID реактива для фильтрации
     */
    private void listBatches(String args) throws Exception {
        List<ReagentBatch> batches;

        if (args.trim().isEmpty()) {
            batches = services.getBatchService().getAll();
            System.out.println("ВСЕ ПАРТИИ:");
        } else {
            try {
                long reagentId = Long.parseLong(args.trim());
                batches = services.getBatchService().getByReagentId(reagentId);
                System.out.println("ПАРТИИ ДЛЯ РЕАКТИВА " + reagentId + ":");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: ID должен быть числом");
                return;
            }
        }

        if (batches.isEmpty()) {
            System.out.println("Партии не найдены");
            return;
        }

        for (ReagentBatch b : batches) {
            String expires = b.getExpiresAt() != null ?
                    b.getExpiresAt().toString().substring(0, 10) : "не указан";
            System.out.printf("ID: %d | %s | %.1f %s | %s | %s | %s%n",
                    b.getId(), b.getLabel(), b.getQuantityCurrent(), b.getUnit(),
                    b.getLocation(), expires, b.getStatus());
        }
    }

    /**
     * Показывает подробную информацию о партии.
     *
     * @param args ID партии
     */
    private void showBatch(String args) {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: batch_show <ID>");
            return;
        }

        try {
            long id = Long.parseLong(args.trim());
            ReagentBatch b = services.getBatchService().getById(id);

            if (b == null) {
                System.out.println("Партия не найдена");
                return;
            }

            System.out.println("\n=== ПАРТИЯ ID: " + id + " ===");
            System.out.println("Метка: " + b.getLabel());
            System.out.println("ID реактива: " + b.getReagentId());
            System.out.println("Количество: " + b.getQuantityCurrent() + " " + b.getUnit());
            System.out.println("Место: " + b.getLocation());
            System.out.println("Срок годности: " + (b.getExpiresAt() != null ? b.getExpiresAt() : "не указан"));
            System.out.println("Статус: " + b.getStatus());
            System.out.println("Владелец: " + b.getOwnerUsername());

            List<StockMove> moves = services.getMoveService().getByBatchId(id, 5);
            if (!moves.isEmpty()) {
                System.out.println("\nПоследние движения:");
                for (StockMove m : moves) {
                    String sign = m.getType() == StockMoveType.IN ? "+" : "-";
                    System.out.printf("  %s %s%.1f %s %s%n",
                            m.getType(), sign, m.getQuantity(), m.getUnit(),
                            m.getReason() != null ? "(" + m.getReason() + ")" : "");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        }
    }

    /**
     * Добавляет движение по партии.
     *
     * @param args ID партии
     */
    private void addMove(String args) throws Exception {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: move_add <ID>");
            return;
        }

        long batchId;
        try {
            batchId = Long.parseLong(args.trim());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            return;
        }

        ReagentBatch batch = services.getBatchService().getById(batchId);
        if (batch == null) {
            System.out.println("Партия не найдена");
            return;
        }

        if (batch.getStatus() == BatchStatus.ARCHIVED) {
            System.out.println("Нельзя добавлять движения к архивной партии");
            return;
        }

        System.out.println("Текущий остаток: " + batch.getQuantityCurrent() + " " + batch.getUnit());

        StockMove move = new StockMove();
        move.setBatchId(batchId);
        move.setUnit(batch.getUnit());

        int typeChoice = input.readChoice("Тип (1-IN, 2-OUT, 3-DISCARD): ", 1, 3);
        switch (typeChoice) {
            case 1: move.setType(StockMoveType.IN); break;
            case 2: move.setType(StockMoveType.OUT); break;
            case 3: move.setType(StockMoveType.DISCARD); break;
        }

        double qty = input.readDouble("Количество: ");
        move.setQuantity(qty);

        String reason = input.readOptional("Причина (пусто если нет): ");
        if (!reason.isEmpty()) move.setReason(reason);

        String owner = input.readString("Владелец (Enter - system): ", false);
        move.setOwnerUsername(owner.isEmpty() ? "system" : owner);

        services.getMoveService().add(move, batch.getQuantityCurrent());

        if (move.getType() == StockMoveType.IN) {
            batch.setQuantityCurrent(batch.getQuantityCurrent() + qty);
        } else {
            batch.setQuantityCurrent(batch.getQuantityCurrent() - qty);
        }

        services.getBatchService().update(batch);

        System.out.println("Движение добавлено. Новый остаток: " +
                batch.getQuantityCurrent() + " " + batch.getUnit());
    }

    /**
     * Показывает историю движений.
     *
     * @param args ID партии и опционально --last N
     */
    private void listMoves(String args) throws Exception {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: move_list <ID> [--last N]");
            return;
        }

        String[] parts = args.trim().split("\\s+");
        long batchId;
        try {
            batchId = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            return;
        }

        ReagentBatch batch = services.getBatchService().getById(batchId);
        if (batch == null) {
            System.out.println("Партия не найдена");
            return;
        }

        int limit = Integer.MAX_VALUE;
        for (int i = 1; i < parts.length; i++) {
            if ("--last".equals(parts[i]) && i + 1 < parts.length) {
                try {
                    limit = Integer.parseInt(parts[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: после --last должно быть число");
                    return;
                }
                break;
            }
        }

        List<StockMove> moves = services.getMoveService().getByBatchId(batchId, limit);

        if (moves.isEmpty()) {
            System.out.println("Движений нет");
            return;
        }

        System.out.println("\nДВИЖЕНИЯ ДЛЯ ПАРТИИ " + batchId);
        System.out.println("Партия: " + batch.getLabel());
        System.out.println("Текущий остаток: " + batch.getQuantityCurrent() + " " + batch.getUnit());
        System.out.println();

        System.out.printf("%-5s %-20s %-8s %-10s %s%n",
                "ID", "Дата", "Тип", "Количество", "Причина");

        for (StockMove m : moves) {
            String date = m.getMovedAt() != null ?
                    m.getMovedAt().toString().substring(0, 19).replace("T", " ") : "не указано";
            String sign = m.getType() == StockMoveType.IN ? "+" : "-";
            System.out.printf("%-5d %-20s %-8s %s%-9s %s%n",
                    m.getId(), date, m.getType(), sign,
                    m.getQuantity() + " " + m.getUnit(),
                    m.getReason() != null ? m.getReason() : "");
        }
    }

    /**
     * Обновляет поля партии.
     *
     * @param args ID партии и поля в формате поле=значение
     */
    private void updateBatch(String args) throws Exception {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: batch_update <ID> поле=значение ...");
            System.out.println("Доступные поля: location, label, status, expiresAt");
            return;
        }

        String[] parts = args.trim().split("\\s+");
        long id;
        try {
            id = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            return;
        }

        ReagentBatch batch = services.getBatchService().getById(id);
        if (batch == null) {
            System.out.println("Партия не найдена");
            return;
        }

        boolean updated = false;

        for (int i = 1; i < parts.length; i++) {
            String[] kv = parts[i].split("=", 2);
            if (kv.length != 2) {
                System.out.println("Пропущено: " + parts[i] + " - нужен формат поле=значение");
                continue;
            }

            String field = kv[0].toLowerCase();
            String value = kv[1];

            try {
                switch (field) {
                    case "location":
                        batch.setLocation(value);
                        updated = true;
                        break;
                    case "label":
                        batch.setLabel(value);
                        updated = true;
                        break;
                    case "status":
                        batch.setStatus(BatchStatus.valueOf(value.toUpperCase()));
                        updated = true;
                        break;
                    case "expiresat":
                        LocalDate date = LocalDate.parse(value);
                        batch.setExpiresAt(date.atStartOfDay(ZoneOffset.UTC).toInstant());
                        updated = true;
                        break;
                    default:
                        System.out.println("Неизвестное поле: " + field);
                }
            } catch (Exception e) {
                System.out.println("Ошибка в поле " + field + ": " + e.getMessage());
            }
        }

        if (updated) {
            services.getBatchService().update(batch);
            System.out.println("Партия обновлена");
        }
    }

    /**
     * Архивирует партию.
     *
     * @param args ID партии
     */
    private void archiveBatch(String args) throws Exception {
        if (args.trim().isEmpty()) {
            System.out.println("Использование: batch_archive <ID>");
            return;
        }

        try {
            long id = Long.parseLong(args.trim());
            ReagentBatch batch = services.getBatchService().getById(id);

            if (batch == null) {
                System.out.println("Партия не найдена");
                return;
            }

            if (batch.getStatus() == BatchStatus.ARCHIVED) {
                System.out.println("Партия уже в архиве");
                return;
            }

            System.out.println("Партия: " + batch.getLabel());
            System.out.println("Остаток: " + batch.getQuantityCurrent() + " " + batch.getUnit());

            if (input.confirm("Архивировать?")) {
                services.getBatchService().archive(id);
                System.out.println("Партия архивирована");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        }
    }

    /**
     * Формирует отчет по складу.
     *
     * @param args может содержать --expires-before ДАТА
     */
    private void stockReport(String args) {
        LocalDate expiresBefore = null;

        if (args.contains("--expires-before")) {
            String[] parts = args.split("--expires-before");
            if (parts.length > 1) {
                try {
                    expiresBefore = LocalDate.parse(parts[1].trim());
                } catch (Exception e) {
                    System.out.println("Ошибка: неверный формат даты");
                    return;
                }
            }
        }

        List<ReagentBatch> batches = services.getBatchService().getAll();
        List<Reagent> reagents = services.getReagentService().getAll();

        long active = batches.stream().filter(b -> b.getStatus() == BatchStatus.ACTIVE).count();
        long archived = batches.size() - active;

        System.out.println("\n=== ОТЧЕТ ПО СКЛАДУ ===");
        System.out.println("Дата: " + LocalDate.now());
        System.out.println("Всего партий: " + batches.size() +
                " (ACTIVE: " + active + ", ARCHIVED: " + archived + ")");
        System.out.println("Всего реактивов: " + reagents.size());

        if (expiresBefore != null) {
            System.out.println("\nПАРТИИ С ИСТЕКАЮЩИМ СРОКОМ (до " + expiresBefore + "):");
            Instant before = expiresBefore.atStartOfDay(ZoneOffset.UTC).toInstant();
            LocalDate now = LocalDate.now();

            for (ReagentBatch b : batches) {
                if (b.getStatus() != BatchStatus.ACTIVE || b.getExpiresAt() == null) continue;
                if (!b.getExpiresAt().isBefore(before)) continue;

                LocalDate expDate = b.getExpiresAt().atZone(ZoneOffset.UTC).toLocalDate();
                long days = java.time.temporal.ChronoUnit.DAYS.between(now, expDate);
                String warn = days < 30 ? " (СКОРО)" : "";

                System.out.printf("ID: %d | %s | %.1f %s | Годен до: %s%s%n",
                        b.getId(), b.getLabel(), b.getQuantityCurrent(), b.getUnit(), expDate, warn);
            }
        }
    }

    /**
     * Очищает все данные.
     */
    private void clearData() throws Exception {
        if (input.confirm("Удалить ВСЕ данные?")) {
            services.clearAll();
            System.out.println("Все данные удалены");
        }
    }

    /**
     * Обрезает строку до указанной длины.
     *
     * @param s строка
     * @param max максимальная длина
     * @return обрезанная строка
     */
    private String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }
}