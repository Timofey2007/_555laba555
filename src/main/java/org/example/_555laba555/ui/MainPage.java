package org.example._555laba555.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.example._555laba555.service.ServiceManager;

public class MainPage extends Application {
    private ServiceManager serviceManager;
    private Stage primaryStage;

    @Override
    public void init() {
        try {
            this.serviceManager = new ServiceManager();
        } catch (Exception e) {
            System.err.println("Критическая ошибка при подключении к БД: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        if (serviceManager == null) {
            showError("Не удалось подключиться к базе данных. Проверьте настройки.");
            return;
        }
        showAuthMenu();
    }

    public void showAuthMenu() {
        MenuView authMenu = new MenuView(this, serviceManager);
        Scene scene = new Scene(authMenu, 400, 300);
        primaryStage.setTitle("Авторизация в системе");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void showMainScreen() {
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();

        tabPane.getTabs().add(new Tab("Реактивы", new ReagView(serviceManager)));
        tabPane.getTabs().add(new Tab("Партии", new BatchView(serviceManager)));
        tabPane.getTabs().add(new Tab("Движения", new MoveView(serviceManager)));
        tabPane.getTabs().add(new Tab("История удалений", new HistoryView(serviceManager)));

        root.setCenter(tabPane);
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root, 1000, 700));
        updateTitle();
        primaryStage.show();
    }

    private void updateTitle() {
        if (serviceManager.getUserService().getCurrentUser() != null) {
            String login = serviceManager.getUserService().getCurrentUser().getLogin();
            primaryStage.setTitle("Система учета — Пользователь: " + login);
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}