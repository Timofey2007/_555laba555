package org.example._555laba555.ui.miniwindows;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example._555laba555.domain.Reagent;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.ui.commands.ReagAddUi;
import org.example._555laba555.validation.ValidationException;

public class AddReagentDialog extends Stage {
    private final ReagAddUi addCommand;

    //поля ввода
    private final TextField nameField = new TextField();
    private final TextField formulaField = new TextField();
    private final TextField casField = new TextField();
    private final TextField hazardField = new TextField();

    public AddReagentDialog(ServiceManager services, Stage owner) {
        this.addCommand = new ReagAddUi(services);

        this.initModality(Modality.WINDOW_MODAL); // Делает окно модальным
        this.initOwner(owner);
        this.setTitle("Добавление нового реактива");

        setupLayout();
    }
    private void setupLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10); grid.setVgap(10);

        grid.add(new Label("Название:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Формула:"), 0, 1);  grid.add(formulaField, 1, 1);
        grid.add(new Label("CAS номер:"), 0, 2); grid.add(casField, 1, 2);
        grid.add(new Label("Класс опасности:"), 0, 3); grid.add(hazardField, 1, 3);

        Button saveBtn = new Button("Сохранить");
        saveBtn.setOnAction(e -> handleSave());

        VBox layout = new VBox(15, grid, saveBtn);
        layout.setPadding(new Insets(10));
        this.setScene(new Scene(layout));
    }
    private void handleSave() {
        try {
            //делегируем выполнение логики классу ReagAddUi
            addCommand.execute(
                    nameField.getText(),
                    formulaField.getText(),
                    casField.getText(),
                    hazardField.getText()
            );

            this.close();

        } catch (ValidationException | IllegalArgumentException ex) {
            //перехватываем ошибки валидации из ReagentValidator или сеттеров Reagent и выводим Alert
            new Alert(Alert.AlertType.ERROR, "Ошибка данных: " + ex.getMessage()).show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Критическая ошибка: " + ex.getMessage()).show();
        }
    }
}
