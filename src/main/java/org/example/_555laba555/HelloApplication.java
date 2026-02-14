package org.example._555laba555;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        Gson gson = new Gson();
        String testJson = gson.toJson(new TestData("JavaFX App", 1));

        Label label = new Label("Gson тест: " + testJson);
        VBox root = new VBox(label);
        Scene scene = new Scene(root, 300, 200);

        stage.setScene(scene);
        stage.setTitle("JavaFX + Gson Test");
        stage.show();

    }

    static class TestData {
        String name;
        int version;
        TestData(String name, int version) {
            this.name = name;
            this.version = version;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}