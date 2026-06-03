package org.example._555laba555.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example._555laba555.fileManager.Conservation;
import org.example._555laba555.service.ServiceManager;

import java.io.File;

public class MainPage extends Application {
    private ServiceManager serviceManager;
    private Conservation conservation;
    private String currentFile;
    private Stage primaryStage;

    @Override
    public void init() {
        this.serviceManager = new ServiceManager();
        try {
            this.currentFile = "records.csv";
            this.conservation = new Conservation(currentFile);
            this.conservation.load(serviceManager);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки в javafx: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showAuthMenu();
    }

    public void showAuthMenu() { //меню авторизации с вызовом класса MenuView
        MenuView authMenu = new MenuView(this, serviceManager, conservation);
        Scene scene = new Scene(authMenu, 400, 300);
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);// нельзя менять мер окна
        primaryStage.show();
    }

    public void showMainScreen() {
        BorderPane root = new BorderPane();
        MenuBar menuBar = new MenuBar();

        Menu fileMnu = new Menu("ФАЙЛ");

        MenuItem loadItem = new MenuItem("Загрузить");
        loadItem.setOnAction(e -> handleLoadFile());

        MenuItem saveItem = new MenuItem("Сохранить");
        saveItem.setOnAction(e -> {
            try { conservation.save(serviceManager); } catch (Exception ex) { showError(ex.getMessage()); }
        });

        fileMnu.getItems().addAll(loadItem, saveItem);
        menuBar.getMenus().add(fileMnu);
        root.setTop(menuBar);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(new Tab("Реактивы", new ReagView(serviceManager, conservation)));
        tabPane.getTabs().add(new Tab("Партии", new BatchView(serviceManager, conservation)));
        tabPane.getTabs().add(new Tab("Движения", new MoveView(serviceManager, conservation)));
        root.setCenter(tabPane);

        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root, 1000, 700));
        updateTitle();
        primaryStage.show();
    }

    private void handleLoadFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                this.currentFile = selectedFile.getAbsolutePath();
                this.conservation = new Conservation(currentFile);
                this.conservation.load(serviceManager);
            } catch (Exception ex) {
                showError("Ошибка при смене файла: " + ex.getMessage());
            }
        }
    }

    private void updateTitle() {
        String login = serviceManager.getUserService().getCurrentUser().getLogin();
        primaryStage.setTitle("Система учета — [" + currentFile + "] Пользователь: "+ login +"");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}