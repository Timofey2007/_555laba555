package org.example._555laba555.ui;

import javafx.stage.Stage;
import org.example._555laba555.service.ServiceManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.example._555laba555.domain.StockMove;
import org.example._555laba555.ui.miniwindows.AddMoveDialog;
import org.example._555laba555.ui.miniwindows.DelMoveDialog;

public class MoveView extends BorderPane {
    private final ServiceManager services;
    private TableView<StockMove> table;

    // Детали движений
    private Label IDVal = new Label();
    private Label BatchIDVal = new Label();
    private Label TypeVal = new Label();
    private Label QuantityVal = new Label();
    private Label ReasonVal = new Label();
    private Label OwnerUsernameVal = new Label();
    private Label MovedAtVal = new Label();
    private Label CreatedAtVal = new Label();

    public MoveView(ServiceManager services) {
        this.services = services;

        // Панелька прослойка для команд
        Button refreshBtn = new Button("Обновить");
        refreshBtn.setOnAction(e -> refreshTable());
        Button addBtn = new Button("Добавить движение");
        addBtn.setOnAction(e -> openAddMoveDialog());
        Button DelBtn = new Button("Удалить");
        DelBtn.setOnAction(e -> openDelMoveDialog());
        this.setTop(new ToolBar(refreshBtn, addBtn, DelBtn));


        // Список движений выводится в левую часть
        table = new TableView<>();

        TableColumn<StockMove, String> colTime = new TableColumn<>("Время движения");
        colTime.setCellValueFactory(new PropertyValueFactory<>("movedAt"));
        colTime.setPrefWidth(150);

        // ДОБАВЛЕНА КОЛОНКА ВЛАДЕЛЬЦА
        TableColumn<StockMove, String> colOwner = new TableColumn<>("Владелец");
        colOwner.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));
        colOwner.setPrefWidth(150);

        table.getColumns().addAll(colTime, colOwner);
        table.setMinWidth(300);

        // Про каждое движение выводится в правую часть
        GridPane details = new GridPane();
        details.setPadding(new Insets(20));
        details.setHgap(10);
        details.setVgap(10);
        details.add(new Label("ID: "), 0, 0);
        details.add(IDVal, 1, 0);
        details.add(new Label("ID Партии: "), 0, 1);
        details.add(BatchIDVal, 1, 1);
        details.add(new Label("Тип: "), 0, 2);
        details.add(TypeVal, 1, 2);
        details.add(new Label("Количество:"), 0, 3);
        details.add(QuantityVal, 1, 3);
        details.add(new Label("Причина: "), 0, 4);
        details.add(ReasonVal, 1, 4);
        details.add(new Label("Владелец: "), 0, 5);
        details.add(OwnerUsernameVal, 1, 5);
        details.add(new Label("Время действия: "), 0, 6);
        details.add(MovedAtVal, 1, 6);
        details.add(new Label("Действие создано: "), 0, 7);
        details.add(CreatedAtVal, 1, 7);

        // Слушатель выбора в таблице
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                IDVal.setText(String.valueOf(newVal.getId()));
                BatchIDVal.setText(String.valueOf(newVal.getBatchId()));
                TypeVal.setText(newVal.getType().toString());
                QuantityVal.setText(newVal.getQuantity() + " " + newVal.getUnit());
                ReasonVal.setText(newVal.getReason() != null ? newVal.getReason() : "не указана");
                OwnerUsernameVal.setText(newVal.getOwnerUsername() != null ? newVal.getOwnerUsername() : "не указан");
                MovedAtVal.setText(newVal.getMovedAt() != null ? newVal.getMovedAt().toString() : "не указано");
                CreatedAtVal.setText(newVal.getCreatedAt() != null ? newVal.getCreatedAt().toString() : "не указано");
            }
        });

        SplitPane splitPane = new SplitPane(table, details);
        splitPane.setDividerPositions(0.3);
        this.setCenter(splitPane);
        refreshTable();
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(services.getMoveService().getAll()));
    }

    private void openAddMoveDialog() {
        AddMoveDialog dialog = new AddMoveDialog(services, (Stage) this.getScene().getWindow());
        dialog.showAndWait();
        refreshTable();
    }
    private void openDelMoveDialog(){
        DelMoveDialog dialog = new DelMoveDialog(services, (Stage) this.getScene().getWindow());
        dialog.showAndWait();;
        refreshTable();
    }
}