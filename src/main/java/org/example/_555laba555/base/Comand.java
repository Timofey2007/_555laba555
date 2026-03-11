package org.example._555laba555.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import static org.example._555laba555.base.BatchStatus.ACTIVE;
import static org.example._555laba555.base.BatchStatus.ARCHIVED;

public class Comand {
    private final ProgramState state;
    private final BufferedReader in;

    public Comand(ProgramState state, BufferedReader in) {
        this.state = state;
        this.in = in;
    }
    private String parseQuery(String args) {
        if (args == null || args.trim().isEmpty()) return null;
        String[] parts = args.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            if ("--q".equals(parts[i]) && i + 1 < parts.length) {
                return parts[i + 1];
            }
        }
        return null;
    }

    private String truncate(String s, int maxLength) {
        if (s == null) return "";
        return s.length() <= maxLength ? s : s.substring(0, maxLength - 3) + "...";
    }

    public void reagAdd() throws IOException {
        Reagent reagent = new Reagent();

        System.out.println("Название:");
        String name = in.readLine();
        System.out.println("Формула:");
        String formula = in.readLine();
        System.out.println("Cas:");
        String cas = in.readLine();
        System.out.println("Класс опасности:");
        String hazardClass = in.readLine();
        System.out.println("Имя владельца:");
        String ownerUsername = in.readLine();

        reagent.setId(System.currentTimeMillis());
        reagent.setName(name);
        reagent.setFormula(formula);
        reagent.setCas(cas);
        reagent.setHazardClass(hazardClass);
        reagent.setOwnerUsername(ownerUsername);
        state.addReagent(reagent);
        System.out.println("Реактив добавлен");
    }

    public void reagList(String args) {
        List<Reagent> reagents = state.getReagents();
        String query = parseQuery(args);

        if (query != null && !query.isEmpty()) {
            final String lowerQuery = query.toLowerCase();
            reagents = reagents.stream()
                    .filter(r -> r.getName().toLowerCase().contains(lowerQuery))
                    .collect(Collectors.toList());
        }

        if (reagents.isEmpty()) {
            System.out.println("Реактивы не найдены.");
            return;
        }

        System.out.printf("%-10s %-20s %-15s %-15s%n", "ID", "Name", "Formula", "CAS");
        System.out.println("-------------------------------------------------------------");
        for (Reagent r : reagents) {
            System.out.printf("%-10d %-20s %-15s %-15s%n",
                    r.getId(),
                    truncate(r.getName(), 20),
                    truncate(r.getFormula(), 15),
                    truncate(r.getCas(), 15));
        }
    }

    public void batchAdd() throws IOException {
        List<Reagent> reagents = state.getReagents();
        if (reagents.isEmpty()) {
            System.out.println("Сначала добавьте реактив (команда reag_add)");
            return;
        }

        System.out.print("Введите ID реактива: ");
        long reagentId;
        try {
            reagentId = Long.parseLong(in.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число");
            return;
        }

        ReagentBatch batch = new ReagentBatch();
        System.out.println("Этикетка(номер партии):");
        String label = in.readLine();
        batch.setLabel(label);

        System.out.print("Текущее количество: ");
        try {
            double quantity = Double.parseDouble(in.readLine());
            batch.setQuantityCurrent(quantity);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число");
            return;
        }

        batch.setId(System.currentTimeMillis());
        batch.setReagentId(reagentId);

        System.out.print("Единицы измерения (G или ML): ");
        String unitStr = in.readLine().toUpperCase();
        try {
            BatchUnit unit = BatchUnit.valueOf(unitStr);
            batch.setUnit(unit);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: введите G или ML");
            return;
        }

        System.out.print("Местоположение: ");
        String location = in.readLine();
        batch.setLocation(location);

        System.out.print("Срок годности (может быть не указан): ");
        String expiresStr = in.readLine();
        if (!expiresStr.trim().isEmpty()) {
            try {
                LocalDate localDate = LocalDate.parse(expiresStr);
                Instant instant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                batch.setExpiresAt(instant);
            } catch (DateTimeParseException e) {
                System.out.println("Ошибка: неверный формат даты. Используйте ГГГГ-ММ-ДД (например 2026-12-31)");
                return;
            }
        } else {
            System.out.println("Срок годности не указан");
        }

        System.out.println("1 = ACTIVE и 2 = ARCHIVED:");
        String status = in.readLine();
        if (status.trim().equals("1")) {
            batch.setStatus(ACTIVE);
        } else if (status.trim().equals("2")) {
            batch.setStatus(ARCHIVED);
        } else {
            System.out.println("Неверный статус, установлен ACTIVE по умолчанию");
            batch.setStatus(ACTIVE);
        }

        System.out.print("Имя владельца: ");
        String owner = in.readLine();
        batch.setOwnerUsername(owner);

        state.addBatch(batch);
        System.out.println("Партия добавлена");
    }

    public void batchList() throws IOException {
        System.out.print("Введите ID реактива (или оставьте пустым для всех): ");
        String input = in.readLine();

        List<ReagentBatch> batches = state.getBatches();

        if (input.trim().isEmpty()) {
            System.out.println("ВСЕ ПАРТИИ");
            for (ReagentBatch b : batches) {
                System.out.println("  ID: " + b.getId() +
                        ", Реактив: " + b.getReagentId() +
                        ", Метка: " + b.getLabel() +
                        ", Количество: " + b.getQuantityCurrent() + " " + b.getUnit() +
                        ", Статус: " + b.getStatus());
            }
        } else {
            try {
                long reagentId = Long.parseLong(input);
                System.out.println("=== ПАРТИИ ДЛЯ РЕАКТИВА " + reagentId + " ===");
                for (ReagentBatch b : batches) {
                    if (b.getReagentId() == reagentId) {
                        System.out.println("  ID: " + b.getId() +
                                ", Метка: " + b.getLabel() +
                                ", Количество: " + b.getQuantityCurrent() + " " + b.getUnit() +
                                ", Место: " + b.getLocation());
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Введите число");
            }
        }
    }

    public void help() {
        System.out.println("""
                1. reag_add - создать новый реактив (интерактивно)
                2) reag_list - список реактивов
                3) batch_add - добавить бутылку/партию (интерактивно)
                4) batch_list [reagent_id] - показать все бутылки по реактиву (или все)
                5) batch_show <batch_id> - карточка бутылки + текущий остаток.
                6) move_add <batch_id> - движение по бутылке (расход/приход/списание), интерактивно.
                7) move_list <batch_id> [--last N] - история движений.
                8) batch_update <batch_id> field=value ... - изменить данные бутылки. (Поля: location, expiresAt, status, label)
                9) batch_archive <batch_id> - архивировать бутылку (например, пустая/списана).
                10) stock_report [--expires-before YYYY-MM-DD] - простой отчёт по складу (например, что скоро просрочится).
                11) clear_data - очистить все данные
                12) exit - выход""");
    }

    public void clearData() throws IOException {
        System.out.print("Вы уверены, что хотите удалить ВСЕ данные? (Y/N): ");
        String confirm = in.readLine();
        if (confirm.equalsIgnoreCase("y")) {
            state.clearAllData();
            System.out.println("Все данные успешно удалены!");
        } else {
            System.out.println("Операция отменена");
        }
    }
}
