package org.example._555laba555;

import com.opencsv.CSVReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.StringReader;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        // Проверка OpenCSV
        try (CSVReader reader = new CSVReader(new StringReader("test,works"))) {
            String[] line = reader.readNext();
            System.out.println("OpenCSV works: " + String.join(",", line));
        } catch (Exception e) {
            System.out.println("OpenCSV error: " + e.getMessage());
        }

        // Минимальное окно
        Label label = new Label("CSV OK");
        stage.setScene(new Scene(label, 200, 100));
        stage.setTitle("Test");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}