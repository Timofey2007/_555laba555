package org.example._555laba555.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.example._555laba555.dataBase.HistoryRepository;
import org.example._555laba555.service.HistoryRecord;
import org.example._555laba555.service.HistoryService;
import org.example._555laba555.service.ServiceManager;

public class HistoryView extends BorderPane {
    private final ServiceManager services;
    private final HistoryService historyService;
    private TableView<HistoryRecord> table;

    public HistoryView(ServiceManager services) {
        this.services = services;
        this.historyService = new HistoryService(
                new HistoryRepository(),
                services.getReagentService(),
                services.getBatchService(),
                services.getMoveService()
        );

        Button refreshBtn = new Button("Обновить");
        refreshBtn.setOnAction(e -> refreshTable());

        VBox top = new VBox(10, refreshBtn);
        top.setPadding(new Insets(10));
        this.setTop(top);

        table = new TableView<>();

        TableColumn<HistoryRecord, String> colTime = new TableColumn<>("Время удаления");
        colTime.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));
        colTime.setPrefWidth(140);

        TableColumn<HistoryRecord, String> colType = new TableColumn<>("Тип");
        colType.setCellValueFactory(new PropertyValueFactory<>("objectType"));
        colType.setPrefWidth(90);

        TableColumn<HistoryRecord, Long> colId = new TableColumn<>("ID объекта");
        colId.setCellValueFactory(new PropertyValueFactory<>("objectId"));
        colId.setPrefWidth(90);

        TableColumn<HistoryRecord, String> colName = new TableColumn<>("Объект");
        colName.setCellValueFactory(new PropertyValueFactory<>("objectDescription"));
        colName.setPrefWidth(150);

        TableColumn<HistoryRecord, String> colWho = new TableColumn<>("Кто удалил");
        colWho.setCellValueFactory(new PropertyValueFactory<>("deletedByName"));
        colWho.setPrefWidth(120);

        TableColumn<HistoryRecord, Void> colAction = new TableColumn<>("Действие");
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button restoreBtn = new Button("Восстановить");
            {
                restoreBtn.setOnAction(e -> {
                    HistoryRecord selected = getTableView().getItems().get(getIndex());
                    long currentUserId = services.getUserService().getCurrentUserId();
                    if (historyService.restore(selected.getId(), currentUserId)) {
                        new Alert(Alert.AlertType.INFORMATION,
                                "Объект успешно восстановлен").showAndWait();
                        refreshTable();
                    } else {
                        new Alert(Alert.AlertType.ERROR,
                                "Ошибка восстановления. Возможно, нет прав.").showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : restoreBtn);
            }
        });
        colAction.setPrefWidth(120);

        table.getColumns().addAll(colTime, colType, colId, colName, colWho, colAction);
        this.setCenter(table);
        refreshTable();
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(historyService.getAll()));
    }
}