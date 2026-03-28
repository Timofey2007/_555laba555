package org.example._555laba555.cli.commands;

import org.example._555laba555.cli.Command;
import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

public class HelpCommand implements Command {

    @Override
    public void justDOIT(ServiceManager services, InputHelper input,
                         Conservation storage, String args) throws Exception {
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
        System.out.println("  save                    - сохранить данные в файл");
        System.out.println("  load [путь]             - загрузить данные из файла");
        System.out.println("  help                    - показать эту справку");
        System.out.println("  exit                    - выход из программы");
        System.out.println("  ui                      - пользовательский интерфейс\n");
    }
}