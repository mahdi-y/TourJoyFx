package com.example.tourjoy;

import javafx.application.Application;
import controllers.registrationController;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage primaryStage;  // Static ²reference to the primary stage

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Assign the incoming stage to your static reference
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/signUp.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 700, 450);
        primaryStage.setTitle("Welcome to TourJoy!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loadFXML(String fxmlFileName) throws IOException {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage is not initialized.");
        }
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFileName));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}

