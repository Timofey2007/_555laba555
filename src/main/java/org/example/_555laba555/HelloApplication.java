package org.example._555laba555;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private TextArea outputArea;
    private TextField inputField;
    private StringBuilder commandHistory = new StringBuilder();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // Центральная область - вывод команд
        outputArea = createOutputArea();
        root.setCenter(outputArea);

        // Нижняя панель - ввод команд
        root.setBottom(createInputPanel());

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Chemical Command Terminal");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Приветственное сообщение
        appendOutput("Реактивы и склад (учёт бутылок и остатков)\n");
        appendOutput("Наберите 'help' для просмотра доступных команд\n");
        appendOutput("> ");
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #333333;");

        // Меню File
        Menu fileMenu = new Menu("File");
        fileMenu.setStyle("-fx-text-fill: white;");

        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(newItem, openItem, saveItem,
                new SeparatorMenuItem(), exitItem);

        // Меню Edit
        Menu editMenu = new Menu("Edit");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");
        MenuItem clearItem = new MenuItem("Clear");
        clearItem.setOnAction(e -> outputArea.clear());

        editMenu.getItems().addAll(copyItem, pasteItem,
                new SeparatorMenuItem(), clearItem);

        // Меню Help
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());

        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
        return menuBar;
    }

    private TextArea createOutputArea() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setFont(Font.font("Consolas", 14));
        textArea.setStyle(
                "-fx-control-inner-background: black;" +
                        "-fx-text-fill: limegreen;" +
                        "-fx-highlight-fill: #00ff00;" +
                        "-fx-highlight-text-fill: black;" +
                        "-fx-font-family: 'Consolas';"
        );

        textArea.setBorder(Border.EMPTY);
        return textArea;
    }

    private HBox createInputPanel() {
        HBox inputPanel = new HBox(5);
        inputPanel.setPadding(new Insets(5));
        inputPanel.setStyle("-fx-background-color: #1e1e1e;");

        // Метка приглашения
        Label promptLabel = new Label("> ");
        promptLabel.setFont(Font.font("Consolas", 14));
        promptLabel.setTextFill(Color.LIMEGREEN);

        // Поле ввода команд
        inputField = new TextField();
        inputField.setFont(Font.font("Consolas", 14));
        inputField.setStyle(
                "-fx-control-inner-background: #1e1e1e;" +
                        "-fx-text-fill: limegreen;" +
                        "-fx-font-family: 'Consolas';" +
                        "-fx-border-color: #444;"
        );
        inputField.setPrefHeight(30);
        inputField.setOnAction(e -> processCommand());

        // Кнопка выполнения
        Button executeButton = new Button("Execute");
        executeButton.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-text-fill: limegreen;" +
                        "-fx-border-color: #444;"
        );
        executeButton.setOnAction(e -> processCommand());

        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputPanel.getChildren().addAll(promptLabel, inputField, executeButton);

        return inputPanel;
    }

    private void processCommand() {
        String command = inputField.getText().trim();
        if (!command.isEmpty()) {
            // Удаляем последнее приглашение и добавляем команду
            String currentText = outputArea.getText();
            if (currentText.endsWith("> ")) {
                outputArea.setText(currentText.substring(0, currentText.length() - 2));
            }

            appendOutput(command);

            // Обрабатываем команду
            executeCommand(command);

            // Новое приглашение
            appendOutput("\n> ");

            // Очищаем поле ввода
            inputField.clear();
        }
    }

    private void executeCommand(String command) {
        switch (command.toLowerCase()) {
            case "help":
                appendOutput("\nAvailable commands:\n");
                appendOutput("  help     - Show this help\n");
                appendOutput("  clear    - Clear screen\n");
                appendOutput("  date     - Show current date\n");
                appendOutput("  time     - Show current time\n");
                appendOutput("  exit     - Close application\n");
                appendOutput("  dir      - List directory (simulated)\n");
                appendOutput("reag_add - создать новый реактив (интерактивно)\n");
                appendOutput("reag_list [--q TEXT] - список реактивов, можно поиск\n");
                appendOutput("batch_add <reagent_id> - добавить бутылку/партию (интерактивно)\n");
                appendOutput("batch_list <reagent_id> [--active] - показать все бутылки по реактиву\n");
                appendOutput("batch_show <batch_id> - карточка бутылки + текущий остаток\n");
                appendOutput("move_add <batch_id> - движение по бутылке (расход/приход/списание), интерактивно\n");
                appendOutput("move_list <batch_id> [--last N] - история движений\n");
                appendOutput("batch_update <batch_id> field=value ... - изменить данные бутылки.  (Поля: location, expiresAt, status, label)\n");
                appendOutput("batch_archive <batch_id> - архивировать бутылку (например, пустая/списана)\n");
                appendOutput("stock_report [--expires-before YYYY-MM-DD] - простой отчёт по складу (например, что скоро просрочится)\n");
                break;

            case "clear":
                outputArea.clear();
                break;

            case "date":
                appendOutput("\n" + java.time.LocalDate.now().toString());
                break;

            case "time":
                appendOutput("\n" + java.time.LocalTime.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
                break;

            case "exit":
                System.exit(0);
                break;
//TODO переделать дир под актауалку
            case "dir":
                appendOutput("\nDirectory of C:\\Users\\User");
                appendOutput("  file1.txt");
                appendOutput("  file2.doc");
                appendOutput("  folder1  <DIR>");
                appendOutput("  folder2  <DIR>");
                break;

            default:
                appendOutput("\nUnknown command: " + command);
                break;
        }
    }

    private void appendOutput(String text) {
        outputArea.appendText(text);
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Command Terminal");
        alert.setContentText("Version 1.0\nCreated with JavaFX");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}