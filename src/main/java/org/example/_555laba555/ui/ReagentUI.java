package org.example._555laba555.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example._555laba555.domain.*;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.fileManager.StorageException;


/**
 * JavaFX интерфейс для управления реактивами.
 * Поддерживает передачу сервисов и хранилища из Main.
 */
public class ReagentUI extends Application {

    private static ServiceManager sharedServices;
    private static Conservation sharedStorage;
    private ServiceManager services;
    private Conservation storage;
    private String dataFile;

    private ListView<Reagent> reagentListView;
    private TextArea detailArea;
    private Label statusLabel;

    /**
     * Запуск JavaFX приложения с передачей параметров.
     */
    public static void launch(Class<?> clazz, ServiceManager services, Conservation storage, String dataFile) {
        // Передаем параметры через статические поля или через конструктор
        // Используем статический метод для передачи данных перед запуском
        Platform.runLater(() -> {
            try {
                ReagentUI app = (ReagentUI) clazz.getDeclaredConstructor().newInstance();
                app.services = services;
                app.storage = storage;
                app.dataFile = dataFile;
                Stage stage = new Stage();
                app.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {
        // Если сервисы не переданы (запуск напрямую), создаем новые
        if (services == null) {
            services = new ServiceManager();
            storage = new Conservation("lab5_data.csv");
            dataFile = "lab5_data.csv";
            try {
                storage.load(services);
            } catch (StorageException e) {
                System.out.println("Предупреждение: " + e.getMessage());
            }
        }

        BorderPane root = new BorderPane();
        root.setTop(createTopPanel());
        root.setCenter(createMasterDetailPanel());
        root.setBottom(createStatusPanel());

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Учет реактивов");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> saveOnExit());
        primaryStage.show();

        refreshData();
    }

    private HBox createTopPanel() {
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));
        topPanel.setStyle("-fx-background-color: #f0f0f0;");

        Button refreshBtn = new Button("Обновить");
        refreshBtn.setOnAction(e -> refreshData());

        Button addBtn = new Button("Добавить");
        addBtn.setOnAction(e -> showAddReagentDialog());

        Button editBtn = new Button("Редактировать");
        editBtn.setOnAction(e -> showEditReagentDialog());

        Button deleteBtn = new Button("Удалить");
        deleteBtn.setOnAction(e -> deleteSelectedReagent());

        Button saveBtn = new Button("Сохранить");
        saveBtn.setOnAction(e -> saveData());

        topPanel.getChildren().addAll(refreshBtn, addBtn, editBtn, deleteBtn, saveBtn);
        return topPanel;
    }

    private SplitPane createMasterDetailPanel() {
        SplitPane splitPane = new SplitPane();

        VBox leftPanel = new VBox(5);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(300);

        Label leftTitle = new Label("Список реактивов");
        leftTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        reagentListView = new ListView<>();
        reagentListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> showReagentDetails(newVal)
        );

        leftPanel.getChildren().addAll(leftTitle, reagentListView);

        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setPrefWidth(500);

        Label rightTitle = new Label("Информация о реактиве");
        rightTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        detailArea = new TextArea();
        detailArea.setEditable(false);
        detailArea.setWrapText(true);
        detailArea.setPrefHeight(400);

        rightPanel.getChildren().addAll(rightTitle, detailArea);

        splitPane.getItems().addAll(leftPanel, rightPanel);
        splitPane.setDividerPositions(0.35);

        return splitPane;
    }

    private HBox createStatusPanel() {
        HBox bottomPanel = new HBox(10);
        bottomPanel.setPadding(new Insets(5, 10, 5, 10));
        bottomPanel.setStyle("-fx-background-color: #e0e0e0;");

        statusLabel = new Label("Готов");
        bottomPanel.getChildren().add(statusLabel);

        return bottomPanel;
    }

    private void refreshData() {
        try {
            ObservableList<Reagent> reagents = FXCollections.observableArrayList(
                    services.getReagentService().getAll()
            );
            reagentListView.setItems(reagents);

            if (!reagents.isEmpty()) {
                reagentListView.getSelectionModel().select(0);
            }

            statusLabel.setText("Загружено: " + reagents.size() + " реактивов");
        } catch (Exception e) {
            statusLabel.setText("Ошибка: " + e.getMessage());
        }
    }

    private void showReagentDetails(Reagent reagent) {
        if (reagent == null) {
            detailArea.clear();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("              КАРТОЧКА РЕАКТИВА\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

        sb.append("ID: ").append(reagent.getId()).append("\n");
        sb.append("Название: ").append(valueOrEmpty(reagent.getName())).append("\n");
        sb.append("Формула: ").append(valueOrEmpty(reagent.getFormula())).append("\n");
        sb.append("CAS: ").append(valueOrEmpty(reagent.getCas())).append("\n");
        sb.append("Класс опасности: ").append(valueOrEmpty(reagent.getHazardClass())).append("\n");
        sb.append("Владелец: ").append(valueOrEmpty(reagent.getOwnerUsername())).append("\n");

        detailArea.setText(sb.toString());
    }

    private void showAddReagentDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Добавить реактив");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField formulaField = new TextField();
        TextField casField = new TextField();
        TextField hazardField = new TextField();
        TextField ownerField = new TextField();

        grid.add(new Label("Название:*"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Формула:"), 0, 1);
        grid.add(formulaField, 1, 1);
        grid.add(new Label("CAS:"), 0, 2);
        grid.add(casField, 1, 2);
        grid.add(new Label("Класс опасности:"), 0, 3);
        grid.add(hazardField, 1, 3);
        grid.add(new Label("Владелец:*"), 0, 4);
        grid.add(ownerField, 1, 4);

        Button saveBtn = new Button("Сохранить");
        Button cancelBtn = new Button("Отмена");

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        grid.add(buttons, 1, 5);

        saveBtn.setOnAction(e -> {
            try {
                if (nameField.getText().trim().isEmpty()) {
                    showAlert("Ошибка", "Название не может быть пустым");
                    return;
                }
                if (ownerField.getText().trim().isEmpty()) {
                    showAlert("Ошибка", "Владелец не может быть пустым");
                    return;
                }

                Reagent r = new Reagent();
                r.setName(nameField.getText().trim());
                r.setFormula(formulaField.getText().trim());
                r.setCas(casField.getText().trim());
                r.setHazardClass(hazardField.getText().trim());
                r.setOwnerUsername(ownerField.getText().trim());

                services.getReagentService().add(r);
                refreshData();
                dialog.close();
                statusLabel.setText("Реактив добавлен");
                saveData();

            } catch (Exception ex) {
                showAlert("Ошибка", ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.show();
    }

    private void showEditReagentDialog() {
        Reagent selected = reagentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите реактив");
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Редактировать реактив");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField(selected.getName());
        TextField formulaField = new TextField(selected.getFormula());
        TextField casField = new TextField(selected.getCas());
        TextField hazardField = new TextField(selected.getHazardClass());
        TextField ownerField = new TextField(selected.getOwnerUsername());

        grid.add(new Label("Название:*"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Формула:"), 0, 1);
        grid.add(formulaField, 1, 1);
        grid.add(new Label("CAS:"), 0, 2);
        grid.add(casField, 1, 2);
        grid.add(new Label("Класс опасности:"), 0, 3);
        grid.add(hazardField, 1, 3);
        grid.add(new Label("Владелец:*"), 0, 4);
        grid.add(ownerField, 1, 4);

        Button saveBtn = new Button("Сохранить");
        Button cancelBtn = new Button("Отмена");

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        grid.add(buttons, 1, 5);

        saveBtn.setOnAction(e -> {
            try {
                if (nameField.getText().trim().isEmpty()) {
                    showAlert("Ошибка", "Название не может быть пустым");
                    return;
                }

                selected.setName(nameField.getText().trim());
                selected.setFormula(formulaField.getText().trim());
                selected.setCas(casField.getText().trim());
                selected.setHazardClass(hazardField.getText().trim());
                selected.setOwnerUsername(ownerField.getText().trim());

                services.getReagentService().update(selected);
                refreshData();
                dialog.close();
                statusLabel.setText("Реактив обновлен");
                saveData();

            } catch (Exception ex) {
                showAlert("Ошибка", ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.show();
    }

    private void deleteSelectedReagent() {
        Reagent selected = reagentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите реактив");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setHeaderText("Удалить реактив?");
        confirm.setContentText("Вы уверены?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    services.getReagentService().remove(selected.getId());
                    refreshData();
                    statusLabel.setText("Реактив удален");
                    saveData();
                } catch (Exception e) {
                    showAlert("Ошибка", e.getMessage());
                }
            }
        });
    }

    private void saveData() {
        try {
            storage.save(services);
            statusLabel.setText("Данные сохранены в " + dataFile);
        } catch (StorageException e) {
            statusLabel.setText("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void saveOnExit() {
        try {
            storage.save(services);
        } catch (StorageException e) {
            System.err.println("Ошибка сохранения при выходе: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String valueOrEmpty(String s) {
        return s == null ? "" : s;
    }
    /**
     * Статический метод для передачи данных из консоли в UI.
     * Вызывается из CommandHandler перед запуском UI.
     *
     * @param services сервисы с данными
     * @param storage хранилище для сохранения
     */
    public static void setServicesAndStorage(ServiceManager services, Conservation storage) {
        sharedServices = services;
        sharedStorage = storage;
    }
    /**
     * Точка входа для запуска UI.
     */
    public static void main(String[] args) {
        launch(args);
    }
}