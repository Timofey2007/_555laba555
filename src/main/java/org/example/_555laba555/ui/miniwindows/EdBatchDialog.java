package org.example._555laba555.ui.miniwindows;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example._555laba555.domain.BatchUnit;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.service.BatchService;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.ui.commands.BatchAddUI;
import org.example._555laba555.ui.commands.BatchEdUI;
import org.example._555laba555.validation.ValidationException;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class EdBatchDialog extends Stage {
    private final BatchEdUI edCommand;

    private final TextField batchIDField = new TextField();
    private final TextField reagentIdField = new TextField();
    private final TextField labelField = new TextField();
    private final TextField quantityField = new TextField();
    private final TextField TypeField = new TextField();
    private final TextField locationField = new TextField();
    private final TextField ExpiresAtField = new TextField();

    public EdBatchDialog(ServiceManager services, Stage owner) {
        this.edCommand = new BatchEdUI(services);
        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(owner);
        this.setTitle("Редактирование партии");
        setupLayout();
    }
    private void setupLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10); grid.setVgap(10);

        grid.add(new Label("ID Партии:"), 0, 0); grid.add(batchIDField, 1, 0);
        grid.add(new Label("ID реактива:"), 0, 1); grid.add(reagentIdField, 1, 1);
        grid.add(new Label("Метка партии:"), 0, 2); grid.add(labelField, 1, 2);
        grid.add(new Label("Количество:"), 0, 3); grid.add(quantityField, 1, 3);
        grid.add(new Label("Ед. измерения(G,ML):"), 0, 4); grid.add(TypeField, 1, 4);
        grid.add(new Label("Место хранения:"), 0, 5); grid.add(locationField, 1, 5);
        grid.add(new Label("Срок годности(XXXX-XX-XX):"), 0, 6); grid.add(ExpiresAtField, 1, 6);
        Button saveBtn = new Button("Сохранить");
        saveBtn.setOnAction(e -> handleSave());

        VBox layout = new VBox(15, grid, saveBtn);
        layout.setPadding(new Insets(10));
        this.setScene(new Scene(layout));
    }
    private void handleSave() {
        try {
            edCommand.execute(
                    Long.parseLong(batchIDField.getText()),
                    Long.parseLong(reagentIdField.getText()),
                    labelField.getText(),
                    Double.parseDouble(quantityField.getText()),
                    BatchUnit.valueOf(TypeField.getText()),
                    locationField.getText(),
                    LocalDate.parse(ExpiresAtField.getText()).atStartOfDay(ZoneOffset.UTC).toInstant()
            );
            this.close();
        } catch (ValidationException | IllegalArgumentException ex) {
            new Alert(Alert.AlertType.ERROR, "Ошибка данных: " + ex.getMessage()).show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Ошибка: введите корректные числа").show();
        }
    }
}
