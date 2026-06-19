package org.example._555laba555.ui.miniwindows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.ui.commands.LoginUI;

public class LoginDialog extends Stage {

    public LoginDialog(ServiceManager services, Stage owner) {
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        setTitle("Вход в систему");

        LoginUI loginCommand = new LoginUI(services);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField loginField = new TextField();
        loginField.setPromptText("Логин");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        Button loginBtn = new Button("Войти");
        loginBtn.setDefaultButton(true);


        grid.add(new Label("Логин:"), 0, 0);
        grid.add(loginField, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(passwordField, 1, 1);

        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(loginBtn);
        grid.add(btnBox, 1, 3);

        loginBtn.setOnAction(e -> {
            String login = loginField.getText();
            String password = passwordField.getText();

            if (login.isEmpty() || password.isEmpty()) {
                showError("Заполните все поля!");
                return;
            }

            try {
                loginCommand.execute(login, password);
                close();
            } catch (Exception ex) {
                showError("Ошибка входа: " + ex.getMessage());
            }
        });


        setScene(new Scene(grid, 300, 150));
        setResizable(false);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}