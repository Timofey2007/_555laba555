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
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.ui.miniwindows.AddMoveDialog;


public class MoveView extends BorderPane {
    private final ServiceManager services;
    private final Conservation storage;
    private TableView<StockMove> table;

    //детали партий
    private Label IDVal = new Label();
    private Label BatchIDVal = new Label();
    private Label TypeVal = new Label();
    private Label QuantityVal = new Label();
    private Label UnitVal = new Label();
    private Label ReasonVal = new Label();
    private Label OwnerUsernameVal = new Label();
    private Label MovedAtVal = new Label();
    private Label CreatedAtVal = new Label();

    public MoveView(ServiceManager services, Conservation storage) {
        this.services = services;
        this.storage = storage;

        //Панелька прослойка для команд
        Button refreshBtn = new Button("Обновить");
        refreshBtn.setOnAction(e -> refreshTable());
        Button addbtn = new Button("Добавит действие");
        addbtn.setOnAction(e -> openAddMoveDiaog());
        this.setTop(new ToolBar(refreshBtn,addbtn));

        //Список реагентов выводиться в левую фигню
        table = new TableView<>();
        TableColumn<StockMove, String> colName = new TableColumn<>("Время дижения");
        colName.setCellValueFactory(new PropertyValueFactory<>("movedAt"));
        table.getColumns().add(colName);
        table.setMinWidth(300);

        //Про каждый реагент выводится в правую панель
        GridPane details = new GridPane();
        details.setPadding(new Insets(20));
        details.setHgap(10); details.setVgap(10);
        details.add(new Label("ID: "), 0, 0); details.add(IDVal, 1, 0);
        details.add(new Label("ID Партии: "), 0, 1); details.add(BatchIDVal, 1, 1);
        details.add(new Label("Тип: "), 0, 2);details.add(TypeVal, 1, 2);
        details.add(new Label("Количество:"), 0, 3); details.add(QuantityVal, 1, 3);
        details.add(new Label("Причина: "), 0, 4); details.add(ReasonVal, 1, 4);
        details.add(new Label("Владелц: "), 0, 5); details.add(OwnerUsernameVal, 1, 5);
        details.add(new Label("Врем действия: "), 0, 6); details.add(MovedAtVal, 1, 6);
        details.add(new Label("Действие создано:  "), 0, 7); details.add(CreatedAtVal, 1, 7);


        //В LavaFX называется слушатель кароче заполняет данные в нашу таблицу
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                IDVal.setText(String.valueOf(newVal.getId()));
                BatchIDVal.setText(String.valueOf(newVal.getId()));
                TypeVal.setText(String.valueOf(newVal.getBatchId()));
                QuantityVal.setText((newVal.getQuantity() + " " + newVal.getUnit()));
                ReasonVal.setText(newVal.getReason());
                OwnerUsernameVal.setText(newVal.getOwnerUsername());
                MovedAtVal.setText(newVal.getMovedAt() != null ? newVal.getMovedAt().toString() : "не указан");
                CreatedAtVal.setText(newVal.getCreatedAt() != null ? newVal.getCreatedAt().toString() : "не указан");
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
    private void openAddMoveDiaog(){
        AddMoveDialog dialog = new AddMoveDialog(services,(Stage) this.getScene().getWindow());
        dialog.showAndWait();
    }
}