package org.example._555laba555.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.ui.miniwindows.LoginDialog;
import org.example._555laba555.ui.miniwindows.RegisterDialog;

public class MenuView extends BorderPane {
    private final MainPage mainPage;
    private final ServiceManager services;

    public MenuView(MainPage mainPage, ServiceManager services) {
        this.mainPage = mainPage;
        this.services = services;

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        Label title = new Label("СИСТЕМА УЧЁТА РЕАКТИВОВ");
        title.setFont(Font.font(20));

        Button loginBtn = new Button("Вход");//вызоа окна логин см команду
        loginBtn.setPrefWidth(200);
        loginBtn.setOnAction(e -> handleLogin());

        Button registerBtn = new Button("Регистрация");//вызоа окна регистрации см команду
        registerBtn.setPrefWidth(200);
        registerBtn.setOnAction(e -> handleRegister());

        root.getChildren().addAll(title, loginBtn, registerBtn);
        this.setCenter(root);
    }

    private void handleLogin() {
        Stage owner = (Stage) this.getScene().getWindow();
        LoginDialog dialog = new LoginDialog(services,  owner);
        dialog.showAndWait(); // Ждём закрытия окна

        // После закрытия проверяем, авторизован ли пользователь
        if (services.getUserService().isAuthenticated()) {
            mainPage.showMainScreen(); // Переключаемся на главное окно
        }
    }

    private void handleRegister() {
        Stage owner = (Stage) this.getScene().getWindow();
        RegisterDialog dialog = new RegisterDialog(services, owner);
        dialog.showAndWait(); // После регистрации пользователь сам нажмёт "Вход"
    }
}