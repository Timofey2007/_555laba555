package org.example._555laba555.base;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ReagentTraker {
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private ProgramState state = new ProgramState();
    private final Comand processor = new Comand(state, in);

    public void run() throws Exception   {
        while (true){
            System.out.println("Введите команду в консоль (коль вам надо что-то вспомнить - напишите help и вам высветяться готовые команды )");

            String line = in.readLine();
            if (line == null) break;

            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            String[] parts = trimmed.split("\\s+", 2);
            String comand = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";

            switch (comand) {
                case "reag_add":
                    processor.reagAdd();
                    break;
                case "reag_list":
                    processor.reagList(args);
                    break;
                case "batch_add":
                    processor.batchAdd();
                    break;
                case "batch_list":
                    processor.batchList();
                    break;
                case "help":
                    processor.help();
                    break;
                case "clear_data":
                    processor.clearData();
                    break;
                case "exit":
                    System.out.println("Выход из программы");
                    return;
            }
        }

    }
}
