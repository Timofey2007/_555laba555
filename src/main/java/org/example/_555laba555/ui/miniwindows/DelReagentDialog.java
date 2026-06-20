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
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.ui.commands.ReagentDelUI;
import org.example._555laba555.validation.ValidationException;

public class DelReagentDialog extends Stage {
    private final ReagentDelUI delCommand;
    private final TextField id = new TextField();

    public DelReagentDialog(ServiceManager services, Stage owner) {
        this.delCommand = new ReagentDelUI(services);
        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(owner);
        this.setTitle("Удаление реактива");
        setupLayout();
    }

    private void setupLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("ID реактива:"), 0, 0);
        grid.add(id, 1, 0);

        Button saveBtn = new Button("Удалить");
        saveBtn.setOnAction(e -> handleSave());

        VBox layout = new VBox(15, grid, saveBtn);
        layout.setPadding(new Insets(10));
        this.setScene(new Scene(layout));
    }

    private void handleSave() {
        try {
            delCommand.execute(Long.parseLong(id.getText()));
            this.close();
        } catch (ValidationException | IllegalArgumentException ex) {
            new Alert(Alert.AlertType.ERROR, "Ошибка данных: " + ex.getMessage()).show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Критическая ошибка: " + ex.getMessage()).show();
        }
    }
}