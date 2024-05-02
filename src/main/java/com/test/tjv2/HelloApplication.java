package com.test.tjv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MonumentFront.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1550, 780);
        stage.setTitle("Tour Joy");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.setProperty("prism.order", "sw"); // Disable hardware acceleration
        launch();
    }
}
