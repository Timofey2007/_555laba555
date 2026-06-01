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
            System.out.println("\nАВТОРИЗАЦИЯ:");
            System.out.println("  register                - регистрация нового пользователя");
            System.out.println("  login                   - вход в систему");
            System.out.println("  logout                  - выход из системы");
            System.out.println("  whoami                  - информация о текущем пользователе");

            System.out.println("\nРАБОТА С РЕАКТИВАМИ:");
            System.out.println("  reag_add                - добавить реактив");
            System.out.println("  reag_list [--q ТЕКСТ]   - список реактивов");
            System.out.println("  reag_del <ID>           - удалить реактив");

            System.out.println("\nРАБОТА С ПАРТИЯМИ:");
            System.out.println("  batch_add               - добавить партию");
            System.out.println("  batch_list [ID]         - список партий");
            System.out.println("  batch_show <ID>         - показать партию");
            System.out.println("  batch_update <ID> поле=значение - обновить партию");
            System.out.println("  batch_archive <ID>      - архивировать партию");
            System.out.println("  batch_del <ID>          - удалить партию");

            System.out.println("\nРАБОТА С ДВИЖЕНИЯМИ:");
            System.out.println("  move_add <ID>           - добавить движение");
            System.out.println("  move_list <ID> [--last N] - список движений");
            System.out.println("  move_del <ID>           - удалить движение");

            System.out.println("\nОТЧЕТЫ:");
            System.out.println("  stock_report [--expires-before ДАТА] - отчет по складу");
            System.out.println("  history_show            - показать историю команд");

            System.out.println("\nФАЙЛЫ:");
            System.out.println("  save                    - сохранить данные");
            System.out.println("  load <путь>             - загрузить данные");

            System.out.println("\nОТМЕНА:");
            System.out.println("  cancel_del              - отменить последнее удаление");

            System.out.println("\nПРОЧЕЕ:");
            System.out.println("  help                    - показать эту справку");
            System.out.println("  exit                    - выход из программы\n");

    }
}