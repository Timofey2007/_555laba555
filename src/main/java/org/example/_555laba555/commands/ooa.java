package org.example._555laba555.commands;

public class ooa {
    public void toStringComm(){
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
    }
}

