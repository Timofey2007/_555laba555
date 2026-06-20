package org.example._555laba555.ui;

import javafx.scene.layout.BorderPane;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.ui.miniwindows.AddReagentDialog;
import org.example._555laba555.ui.miniwindows.DelReagentDialog;

public class ReagView extends BorderPane {
    private final ServiceManager services;
    private TableView<Reagent> table;

    private Label IDVal = new Label();
    private Label nameVal = new Label();
    private Label formulaVal = new Label();
    private Label casVal = new Label();
    private Label hazardVal = new Label();
    private Label ownerVal = new Label();

    public ReagView(ServiceManager services) {
        this.services = services;

        Button refreshBtn = new Button("Обновить");
        refreshBtn.setOnAction(e -> refreshTable());
        Button addreagBtn = new Button("Добавить реагент");
        addreagBtn.setOnAction(e -> openAddReagentDialog());
        Button delreagBtn = new Button("Удалить реагент");
        delreagBtn.setOnAction(e -> openDelReagentDialog());
        this.setTop(new ToolBar(refreshBtn, addreagBtn, delreagBtn));


        table = new TableView<>();
        TableColumn<Reagent, String> colName = new TableColumn<>("Название");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Reagent, String> colOwner = new TableColumn<>("Владелец");
        colOwner.setCellValueFactory(new PropertyValueFactory<>("ownerName"));

        table.getColumns().addAll(colName, colOwner);
        table.setMinWidth(300);

        GridPane details = new GridPane();
        details.setPadding(new Insets(20));
        details.setHgap(10); details.setVgap(10);
        details.add(new Label("ID:"), 0, 0); details.add(IDVal, 1, 0);
        details.add(new Label("Название:"), 0, 1); details.add(nameVal, 1, 1);
        details.add(new Label("Формула:"), 0, 2); details.add(formulaVal, 1, 2);
        details.add(new Label("CAS:"), 0, 3); details.add(casVal, 1, 3);
        details.add(new Label("Опасность:"), 0, 4); details.add(hazardVal, 1, 4);
        details.add(new Label("Владелец:"), 0, 5); details.add(ownerVal, 1, 5);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                IDVal.setText(String.valueOf(newVal.getId()));
                nameVal.setText(newVal.getName());
                formulaVal.setText(newVal.getFormula());
                casVal.setText(newVal.getCas());
                hazardVal.setText(newVal.getHazardClass());
                ownerVal.setText(newVal.getOwnerName() != null ? newVal.getOwnerName() : "Unknown");
            }
        });

        SplitPane splitPane = new SplitPane(table, details);
        splitPane.setDividerPositions(0.3);
        this.setCenter(splitPane);
        refreshTable();
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(services.getReagentService().getAll()));
    }

    private void openAddReagentDialog() {
        AddReagentDialog dialog = new AddReagentDialog(services, (Stage) this.getScene().getWindow());
        dialog.showAndWait();
        refreshTable();
    }
    private void openDelReagentDialog() {
        DelReagentDialog dialog = new DelReagentDialog(services, (Stage) this.getScene().getWindow());
        dialog.showAndWait();
        refreshTable();
    }
}