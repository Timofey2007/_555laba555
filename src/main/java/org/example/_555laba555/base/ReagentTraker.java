package org.example._555laba555.base;
import org.example._555laba555.base.ProgramState.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;

import static org.example._555laba555.base.BatchStatus.ACTIVE;
import static org.example._555laba555.base.BatchStatus.ARCHIVED;

public class ReagentTraker {
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private ProgramState state = new ProgramState();

    public void run() throws Exception   {
        while (true){
            System.out.println("Введите команду в консоль (коль вам надо что-то вспомнить - напишите help и вам высветяться готовые команды )");
            String line = in.readLine();
            if (line == null) break;
            if (line.trim().equals("reag_add")){
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
                continue;
            }
            else if (line.trim().equals("reag_list")){
                for (Reagent r : state.getReagents()) {
                    System.out.println("  " + r.getId() + ": " + r.getName());
                }
                continue;
            } else if (line.trim().equals("batch_add")) {
                List<Reagent> reagents = state.getReagents();
                ReagentBatch batch = new ReagentBatch();

                if (reagents.isEmpty()) {
                    System.out.println("Сначала добавьте реактив (команда reag_add)");
                    continue;
                }

                System.out.print("Введите ID реактива: ");
                long reagentId;
                try {
                    reagentId = Long.parseLong(in.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите число");
                    continue;
                }

                System.out.println("Этикетка(номер партии):");
                String label = in.readLine();
                batch.setLabel(label);

                System.out.print("Текущее количество: ");
                try {
                    double quantity = Double.parseDouble(in.readLine());
                    batch.setQuantityCurrent(quantity);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите число");
                    continue;
                }

                //Создаем новую партию
                batch.setId(System.currentTimeMillis());
                batch.setReagentId(reagentId);

                System.out.print("Единицы измерения (G или ML): ");
                String unitStr = in.readLine().toUpperCase();
                try {
                    BatchUnit unit = BatchUnit.valueOf(unitStr);
                    batch.setUnit(unit);
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: введите G или ML");
                    continue;
                }
                System.out.print("Местоположение: ");
                String location = in.readLine();
                batch.setLocation(location);

                System.out.print("Срок годности (может быть не указан): ");
                String expiresStr = in.readLine();
                if (!expiresStr.trim().isEmpty()) {
                    try{
                    java.time.LocalDate localDate = java.time.LocalDate.parse(expiresStr);
                    java.time.Instant instant = localDate.atStartOfDay(java.time.ZoneOffset.UTC).toInstant();
                    batch.setExpiresAt(instant);}
                    catch (java.time.format.DateTimeParseException e){
                        System.out.println("Ошибка: неверный формат даты. Используйте ГГГГ-ММ-ДД (например 2026-12-31)");
                    }
                }else {
                    System.out.println("Срок годности не указан");
                }

                System.out.println("1 = ACTIVE и 2 = ARCHIVED:");
                String status = in.readLine();
                if(status.trim().equals("1")){
                    batch.setStatus(ACTIVE);
                } else if(status.trim().equals("2")){
                    batch.setStatus(ARCHIVED);
                }

                System.out.print("Имя владельца: ");
                String owner = in.readLine();
                batch.setOwnerUsername(owner);


                state.addBatch(batch);
                System.out.println("Партия добавлена (си цзин пин не гордится тобой)");
                continue;
            }

            /**
             * Вывод списка партий по их id
             */
            else if (line.trim().equals("batch_list")) {
                System.out.print("Введите ID реактива (или оставьте пустым для всех): ");
                String input = in.readLine();

                List<ReagentBatch> batches = state.getBatches();

                if (input.trim().isEmpty()) {
                    // Показываем все партии
                    System.out.println("ВСЕ ПАРТИИ");
                    for (ReagentBatch b : batches) {
                        System.out.println("  ID: " + b.getId() +
                                ", Реактив: " + b.getReagentId() +
                                ", Метка: " + b.getLabel() +
                                ", Количество: " + b.getQuantityCurrent() + " " + b.getUnit() +
                                ", Статус: " + b.getStatus());
                    }
                } else {
                    // Показываем партии для конкретного реактива
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
                continue;
            }

            else if (line.equals("help")){
                System.out.println("1. reag_add - создать новый реактив (интерактивно)\n" +
                        "2) reag_list [--q TEXT] - список реактивов, можно поиск.\n" +
                        "3) batch_add <reagent_id> - добавить бутылку/партию (интерактивно).\n" +
                        "4) batch_list <reagent_id> [--active] - показать все бутылки по реактиву.\n" +
                        "5) batch_show <batch_id> - карточка бутылки + текущий остаток.\n" +
                        "6) move_add <batch_id> - движение по бутылке (расход/приход/списание), интерактивно.\n" +
                        "7) move_list <batch_id> [--last N] - история движений.\n" +
                        "8) batch_update <batch_id> field=value ... - изменить данные бутылки.  (Поля: location, expiresAt, status, label)\n" +
                        "9) batch_archive <batch_id> - архивировать бутылку (например, пустая/списана).\n" +
                        "10) stock_report [--expires-before YYYY-MM-DD] - простой отчёт по складу (например, что скоро просрочится)." );
                continue;
            }

            else if (line.equals("exit")){
                System.out.println("Выход из программы");
                break;
            }

            else if(line.trim().equals("clear_data")){
                System.out.print("Вы уверены, что хотите удалить ВСЕ данные? (Y/N): ");
                String confirm = in.readLine();
                if (confirm.equalsIgnoreCase("y")) {
                    state.clearAllData();
                    System.out.println("Все данные успешно удалены!");
                } else {
                    System.out.println("Операция отменена");
                }
                continue;
            }
        }

    }
}
