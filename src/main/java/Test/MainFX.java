package Test;

import Controller.AddCountry;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainFX.class.getResource("Country.fxml"));
        Parent root = fxmlLoader.load();
        AddCountry Controller = fxmlLoader.getController(); // Get the controller instance
        Scene scene = new Scene(root, 700, 390);
        stage.setTitle("Country Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
