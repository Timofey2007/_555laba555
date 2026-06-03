package org.example._555laba555.ui.miniwindows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.ui.commands.RegisterUI;

public class RegisterDialog extends Stage {

    public RegisterDialog(ServiceManager services, Conservation storage, Stage owner) {
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        setTitle("Регистрация");

        RegisterUI registerCommand = new RegisterUI(services);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField loginField = new TextField();
        loginField.setPromptText("Придумайте логин");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Придумайте пароль");

        Button registerBtn = new Button("Зарегистрироваться");
        registerBtn.setDefaultButton(true);


        grid.add(new Label("Логин:"), 0, 0);
        grid.add(loginField, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(passwordField, 1, 1);

        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(registerBtn);
        grid.add(btnBox, 1, 3);

        registerBtn.setOnAction(e -> {
            String login = loginField.getText();
            String password = passwordField.getText();

            if (login.isEmpty() || password.isEmpty()) {
                showError("Заполните все поля!");
                return;
            }

            try {
                registerCommand.execute(login, password); // Вызываем команду
                new Alert(Alert.AlertType.INFORMATION, "Регистрация успешна!").showAndWait();
                close();
            } catch (Exception ex) {
                showError("Ошибка регистрации: " + ex.getMessage());
            }
        });


        setScene(new Scene(grid, 300, 150));
        setResizable(false);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}