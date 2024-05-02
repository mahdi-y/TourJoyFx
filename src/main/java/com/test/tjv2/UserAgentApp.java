package com.test.tjv2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.web.WebView;

public class UserAgentApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        StackPane root = new StackPane();
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        Platform.runLater(() -> {
            String userAgent = webView.getEngine().getUserAgent();
            System.out.println("User Agent: " + userAgent);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
