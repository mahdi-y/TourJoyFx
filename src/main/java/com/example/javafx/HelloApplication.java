package com.example.javafx;

import controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/javafx/Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 390);
        Home Controller = fxmlLoader.getController(); // Get the controller instance
        stage.setTitle("TourJoy");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}