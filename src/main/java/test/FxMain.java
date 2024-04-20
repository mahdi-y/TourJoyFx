package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxMain extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create an instance of FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontoffice.fxml"));

        // Load the FXML file
        Parent root = loader.load();
        // Set the title of the stage
        primaryStage.setTitle("Tour Joy");

        // Create a scene with the loaded FXML file
        Scene scene = new Scene(root);

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }
}
