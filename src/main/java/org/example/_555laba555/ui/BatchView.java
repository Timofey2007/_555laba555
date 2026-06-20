package org.example._555laba555.ui;

import javafx.scene.layout.BorderPane;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.ui.miniwindows.AddBatchDialog;
import org.example._555laba555.ui.miniwindows.DelBatchDialog;
import org.example._555laba555.ui.miniwindows.EdBatchDialog;

public class BatchView extends BorderPane {
    private final ServiceManager services;
    private TableView<ReagentBatch> table;

    // Детали партий
    private Label IDVal = new Label();
    private Label LableVal = new Label();
    private Label ReagIDVal = new Label();
    private Label ColVoVal = new Label();
    private Label PlaseVal = new Label();
    private Label ExpiresAtVal = new Label();
    private Label StatusVal = new Label();
    private Label OwnerVal = new Label();

    public BatchView(ServiceManager services) {
        this.services = services;

        // Панелька прослойка для команд
        Button refreshBtn = new Button("Обновить");
        refreshBtn.setOnAction(e -> refreshTable());
        Button addBtn = new Button("Добавить партию");
        addBtn.setOnAction(e -> openAddBatchDialog());
        Button delBtn = new Button("Удалить");
        delBtn.setOnAction(e -> openDelBatchDialog());
        Button edBtn = new Button("Изменить");
        edBtn.setOnAction(e -> openEdBatchDialog());
        this.setTop(new ToolBar(refreshBtn, addBtn, delBtn,edBtn));

        // Список партий выводится в левую часть
        table = new TableView<>();

        TableColumn<ReagentBatch, String> colLabel = new TableColumn<>("Метка");
        colLabel.setCellValueFactory(new PropertyValueFactory<>("label"));
        colLabel.setPrefWidth(150);

        TableColumn<ReagentBatch, String> colOwner = new TableColumn<>("Владелец");
        colOwner.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));
        colOwner.setPrefWidth(150);

        table.getColumns().addAll(colLabel, colOwner);
        table.setMinWidth(300);

        // Про каждую партию выводится в правую часть
        GridPane details = new GridPane();
        details.setPadding(new Insets(20));
        details.setHgap(10);
        details.setVgap(10);
        details.add(new Label("ID: "), 0, 0);
        details.add(IDVal, 1, 0);
        details.add(new Label("Метка: "), 0, 1);
        details.add(LableVal, 1, 1);
        details.add(new Label("ID реактива: "), 0, 2);
        details.add(ReagIDVal, 1, 2);
        details.add(new Label("Количество:"), 0, 3);
        details.add(ColVoVal, 1, 3);
        details.add(new Label("Место: "), 0, 4);
        details.add(PlaseVal, 1, 4);
        details.add(new Label("Срок годности: "), 0, 5);
        details.add(ExpiresAtVal, 1, 5);
        details.add(new Label("Статус: "), 0, 6);
        details.add(StatusVal, 1, 6);
        details.add(new Label("Владелец: "), 0, 7);
        details.add(OwnerVal, 1, 7);

        // Слушатель выбора в таблице
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                IDVal.setText(String.valueOf(newVal.getId()));
                LableVal.setText(newVal.getLabel());
                ReagIDVal.setText(String.valueOf(newVal.getReagentId()));
                ColVoVal.setText(newVal.getQuantityCurrent() + " " + newVal.getUnit());
                PlaseVal.setText(newVal.getLocation());
                ExpiresAtVal.setText(newVal.getExpiresAt() != null ? newVal.getExpiresAt().toString() : "не указан");
                StatusVal.setText(newVal.getStatus().toString());
                OwnerVal.setText(newVal.getOwnerUsername() != null ? newVal.getOwnerUsername() : "не указан");
            }
        });

        SplitPane splitPane = new SplitPane(table, details);
        splitPane.setDividerPositions(0.3);
        this.setCenter(splitPane);
        refreshTable();
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(services.getBatchService().getAll()));
    }

    private void openAddBatchDialog() {
        AddBatchDialog dialog = new AddBatchDialog(services, (Stage) this.getScene().getWindow());
        dialog.showAndWait();
        refreshTable();
    }
    private void openDelBatchDialog(){
        DelBatchDialog dialog = new DelBatchDialog(services, (Stage) this.getScene().getWindow());
        dialog.showAndWait();
        refreshTable();
    }
    private void openEdBatchDialog(){
        EdBatchDialog dialog = new EdBatchDialog(services, (Stage) this.getScene().getWindow());
        dialog.showAndWait();
        refreshTable();
    }
}