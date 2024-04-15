package com.example.tourjoy;

import javafx.application.Application;
import controllers.registrationController;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/signUp.fxml"));
        Parent root = fxmlLoader.load(); // Load the FXML file once

        registrationController controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 700, 450); // Use the loaded root node
        stage.setTitle("Welcome to TourJoy!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
