package org.example._555laba555.cli;

import org.example._555laba555.cli.commands.*;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.validation.ValidationException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    private final ServiceManager services;
    private final InputHelper input;
    private final Conservation storage;
    private boolean running;
    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler(ServiceManager services, Conservation storage) {
        this.services = services;
        this.storage = storage;
        this.input = new InputHelper(new BufferedReader(new InputStreamReader(System.in)));
        this.running = true;
        initCommands();
    }
    private void initCommands() {
        commands.put("help", new HelpCommand());
        commands.put("exit", new ExitCommand());
        commands.put("reag_add", new ReagAddCommand());
        commands.put("reag_list", new ReagListCommand());
        commands.put("batch_add", new BatchAddCommand());
        commands.put("batch_list", new BatchListCommand());
        commands.put("batch_show", new BatchShowCommand());
        commands.put("move_add", new MoveAddCommand());
        commands.put("move_list", new MoveListCommand());
        commands.put("batch_update", new BatchUpdateCommand());
        commands.put("batch_archive", new BatchArchiveCommand());
        commands.put("stock_report", new StockReportCommand());
        commands.put("save", new SaveCommand());
        commands.put("load", new LoadCommand());
        commands.put("move_del", new MoveDelete());
        commands.put("reag_del", new ReagentDelete());
        commands.put("batch_del", new BatchDelete());
        commands.put("history_show", new HistoryShow());
        commands.put("cancel_del", new DeletingOfDeleting());
        commands.put("register", new RegisterCommand());
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("whoami", new WhoamiCommand());




    }

    public void run() {
        System.out.println("Программа учета реактивов");
        System.out.println("Введите help для списка команд");
        System.out.println("Для выхода введите exit\n");

        while (running) {
            try {
                System.out.print("> ");
                String line = input.readString("", false);
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String args = parts.length > 1 ? parts[1] : "";

                Command cmd = commands.get(commandName);
                if (cmd != null) {
                    cmd.justDOIT(services, input, storage, args);
                } else {
                    System.out.println("Неизвестная команда. Введите help");
                }

            } catch (ValidationException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}