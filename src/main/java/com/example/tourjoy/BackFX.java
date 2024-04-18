package com.example.tourjoy;

import controllers.BackController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BackFX extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("back.fxml"));
        Parent root = fxmlLoader.load(); // Load the FXML file once

        BackController controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 700, 450); // Use the loaded root node
        stage.setTitle("Welcome to TourJoy!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
