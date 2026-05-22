package org.example._555laba555.ui.miniwindows;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example._555laba555.domain.BatchUnit;
import org.example._555laba555.domain.StockMoveType;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.ui.commands.MoveAddUI;
import org.example._555laba555.validation.ValidationException;

public class AddMoveDialog extends Stage {
    private final MoveAddUI addCommand;

    private final TextField batchIdField = new TextField();
    private final TextField typeField = new TextField();
    private final TextField quantityField = new TextField();
    private final TextField unitField = new TextField();
    private final TextField reasonField = new TextField();

    public AddMoveDialog(ServiceManager services, Stage owner) {
        this.addCommand = new MoveAddUI(services);
        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(owner);
        this.setTitle("Регистрация движения");
        setupLayout();
    }

    private void setupLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10); grid.setVgap(10);

        grid.add(new Label("ID партии:"), 0, 0); grid.add(batchIdField, 1, 0);
        grid.add(new Label("Тип операции(IN,OUT,DISCARD):"), 0, 1); grid.add(typeField, 1, 1);
        grid.add(new Label("Количество:"), 0, 2); grid.add(quantityField, 1, 2);
        grid.add(new Label("Ед. измерения(G,ML):"), 0, 3); grid.add(unitField, 1, 3);
        grid.add(new Label("Причина:"), 0, 4); grid.add(reasonField, 1, 4);

        Button saveBtn = new Button("Выполнить");
        saveBtn.setOnAction(e -> handleSave());

        VBox layout = new VBox(15, grid, saveBtn);
        layout.setPadding(new Insets(10));
        this.setScene(new Scene(layout));
    }

    private void handleSave() {
        try {
            addCommand.execute(
                    Long.parseLong(batchIdField.getText()),
                    StockMoveType.valueOf(typeField.getText()),
                    Double.parseDouble(quantityField.getText()),
                    BatchUnit.valueOf(unitField.getText()),
                    reasonField.getText()
            );
            this.close();
        } catch (ValidationException | IllegalArgumentException ex) {
            new Alert(Alert.AlertType.ERROR, "Ошибка операции: " + ex.getMessage()).show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Ошибка ввода: проверьте числовые поля").show();
        }
    }
}