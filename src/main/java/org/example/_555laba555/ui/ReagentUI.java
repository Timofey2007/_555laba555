package org.example._555laba555.ui;

import javafx.application.Application;
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


/**
 * JavaFX интерфейс в стиле Master-Detail для управления реактивами.
 * Левая панель: список всех реактивов
 * Правая панель: детальная информация о выбранном реактиве
 */
public class ReagentUI extends Application {

    private ServiceManager services;
    private ListView<Reagent> reagentListView;
    private TextArea detailArea;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        services = new ServiceManager();

        // Главный контейнер
        BorderPane root = new BorderPane();

        // Верхняя панель с кнопками
        root.setTop(createTopPanel());

        // Центральная панель (Master-Detail)
        root.setCenter(createMasterDetailPanel());

        // Нижняя панель со статусом
        root.setBottom(createStatusPanel());

        // Создаем сцену
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Учет реактивов - Master-Detail");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Загружаем данные
        refreshData();
    }

    /**
     * Верхняя панель с кнопками управления.
     */
    private HBox createTopPanel() {
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));
        topPanel.setStyle("-fx-background-color: #f0f0f0;");

        Button refreshBtn = new Button("Обновить (Refresh)");
        refreshBtn.setOnAction(e -> refreshData());
        refreshBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        Button addBtn = new Button("Добавить реактив");
        addBtn.setOnAction(e -> showAddReagentDialog());

        Button editBtn = new Button("Редактировать");
        editBtn.setOnAction(e -> showEditReagentDialog());

        Button deleteBtn = new Button("Удалить");
        deleteBtn.setOnAction(e -> deleteSelectedReagent());

        Button batchBtn = new Button("Показать партии");
        batchBtn.setOnAction(e -> showBatchesForSelected());

        topPanel.getChildren().addAll(refreshBtn, addBtn, editBtn, deleteBtn, batchBtn);

        return topPanel;
    }

    /**
     * Центральная панель: слева список, справа детали.
     */
    private SplitPane createMasterDetailPanel() {
        SplitPane splitPane = new SplitPane();

        // Левая панель - список реактивов
        VBox leftPanel = new VBox(5);
        leftPanel.setPadding(new Insets(10));

        Label leftTitle = new Label("Список реактивов");
        leftTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        reagentListView = new ListView<>();
        reagentListView.setPrefWidth(300);
        reagentListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> showReagentDetails(newVal)
        );

        leftPanel.getChildren().addAll(leftTitle, reagentListView);

        // Правая панель - детальная информация
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

    /**
     * Нижняя панель со статусом.
     */
    private HBox createStatusPanel() {
        HBox bottomPanel = new HBox(10);
        bottomPanel.setPadding(new Insets(5, 10, 5, 10));
        bottomPanel.setStyle("-fx-background-color: #e0e0e0;");

        statusLabel = new Label("Готов");
        bottomPanel.getChildren().add(statusLabel);

        return bottomPanel;
    }

    /**
     * Обновляет данные из сервисов и перерисовывает список.
     */
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
            statusLabel.setText("Ошибка загрузки: " + e.getMessage());
        }
    }

    /**
     * Показывает детальную информацию о выбранном реактиве.
     */
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
        sb.append("CAS номер: ").append(valueOrEmpty(reagent.getCas())).append("\n");
        sb.append("Класс опасности: ").append(valueOrEmpty(reagent.getHazardClass())).append("\n");
        sb.append("Владелец: ").append(valueOrEmpty(reagent.getOwnerUsername())).append("\n");

        if (reagent.getCreatedAt() != null) {
            sb.append("Создан: ").append(reagent.getCreatedAt()).append("\n");
        }
        if (reagent.getUpdatedAt() != null) {
            sb.append("Обновлен: ").append(reagent.getUpdatedAt()).append("\n");
        }

        // Показываем партии для этого реактива
        sb.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        sb.append("              ПАРТИИ РЕАКТИВА\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        boolean hasBatches = false;
        for (ReagentBatch b : services.getBatchService().getAll()) {
            if (b.getReagentId() == reagent.getId()) {
                hasBatches = true;
                sb.append("\nID: ").append(b.getId());
                sb.append("\n  Метка: ").append(b.getLabel());
                sb.append("\n  Количество: ").append(b.getQuantityCurrent())
                        .append(" ").append(b.getUnit());
                sb.append("\n  Место: ").append(b.getLocation());
                sb.append("\n  Статус: ").append(b.getStatus());
                if (b.getExpiresAt() != null) {
                    sb.append("\n  Срок годности: ").append(b.getExpiresAt());
                }
                sb.append("\n");
            }
        }

        if (!hasBatches) {
            sb.append("\nНет партий для этого реактива.\n");
        }

        detailArea.setText(sb.toString());
    }

    /**
     * Диалог добавления нового реактива.
     */
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

            } catch (Exception ex) {
                showAlert("Ошибка", ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Диалог редактирования выбранного реактива.
     */
    private void showEditReagentDialog() {
        Reagent selected = reagentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите реактив для редактирования");
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

            } catch (Exception ex) {
                showAlert("Ошибка", ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> dialog.close());

        Scene scene = new Scene(grid, 400, 250);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Удаляет выбранный реактив.
     */
    private void deleteSelectedReagent() {
        Reagent selected = reagentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите реактив для удаления");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Подтверждение");
        confirm.setHeaderText("Удалить реактив?");
        confirm.setContentText("Вы уверены, что хотите удалить реактив \"" +
                selected.getName() + "\"?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    services.getReagentService().remove(selected.getId());
                    refreshData();
                    statusLabel.setText("Реактив удален");
                } catch (Exception e) {
                    showAlert("Ошибка", e.getMessage());
                }
            }
        });
    }

    /**
     * Показывает партии для выбранного реактива.
     */
    private void showBatchesForSelected() {
        Reagent selected = reagentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Ошибка", "Выберите реактив");
            return;
        }

        Stage dialog = new Stage();
        dialog.setTitle("Партии для " + selected.getName());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        ListView<ReagentBatch> batchListView = new ListView<>();

        ObservableList<ReagentBatch> batches = FXCollections.observableArrayList(
                services.getBatchService().getByReagentId(selected.getId())
        );
        batchListView.setItems(batches);

        batchListView.setCellFactory(lv -> new ListCell<ReagentBatch>() {
            @Override
            protected void updateItem(ReagentBatch b, boolean empty) {
                super.updateItem(b, empty);
                if (empty || b == null) {
                    setText(null);
                } else {
                    setText(b.getLabel() + " | " + b.getQuantityCurrent() +
                            " " + b.getUnit() + " | " + b.getLocation());
                }
            }
        });

        Label info = new Label("Всего партий: " + batches.size());
        vbox.getChildren().addAll(info, batchListView);

        Scene scene = new Scene(vbox, 500, 400);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Показывает всплывающее сообщение об ошибке.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Преобразует null в пустую строку.
     */
    private String valueOrEmpty(String s) {
        return s == null ? "" : s;
    }

    /**
     * Точка входа в JavaFX приложение.
     */
    public static void main(String[] args) {
        launch(args);
    }
}