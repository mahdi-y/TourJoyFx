package com.example.tourjoy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ProgressIndicator;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage primaryStage;
    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        primaryStage.initStyle(StageStyle.TRANSPARENT);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/signUp.fxml"));
        Parent root = fxmlLoader.load();

        VBox mainContainer = new VBox();
        mainContainer.getStyleClass().add("mainContainer");

        HBox titleBar = createTitleBar();

        mainContainer.getChildren().addAll(titleBar, root);

        titleBar.setOnMousePressed(this::handleMousePressed);
        titleBar.setOnMouseDragged(this::handleMouseDragged);

        Scene scene = new Scene(mainContainer, 700, 700);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static Node createLoader() {
        ProgressIndicator loader = new ProgressIndicator();
        loader.setPrefSize(100, 100);
        return loader;
    }

    private static HBox createTitleBar() {
        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("title-bar");
        titleBar.setAlignment(Pos.CENTER_RIGHT);
        titleBar.setPadding(new Insets(5));


        Button closeButton = new Button();
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(event -> primaryStage.close());

        Button expandButton = new Button();
        expandButton.getStyleClass().add("expand-button");
        expandButton.setOnAction(event -> primaryStage.setMaximized(!primaryStage.isMaximized()));


        Button minimizeButton = new Button();
        minimizeButton.getStyleClass().add("minimize-button");
        minimizeButton.setOnAction(event -> primaryStage.setIconified(true));

        titleBar.getChildren().addAll(minimizeButton, expandButton, closeButton);

        return titleBar;
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = primaryStage.getX() - event.getScreenX();
        yOffset = primaryStage.getY() - event.getScreenY();
    }

    private void handleMouseDragged(MouseEvent event) {
        primaryStage.setX(event.getScreenX() + xOffset);
        primaryStage.setY(event.getScreenY() + yOffset);
    }


    public static void loadFXML(String fxmlFileName) throws IOException {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage is not initialized.");
        }

        VBox loadingContainer = new VBox(createLoader());
        loadingContainer.setAlignment(Pos.CENTER);
        Scene loadingScene = new Scene(loadingContainer, 700, 700);
        loadingScene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(loadingScene);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFileName));
                Parent content = loader.load();

                HBox titleBar = createTitleBar();
                titleBar.setOnMousePressed(event -> {
                    xOffset = primaryStage.getX() - event.getScreenX();
                    yOffset = primaryStage.getY() - event.getScreenY();
                });
                titleBar.setOnMouseDragged(event -> {
                    primaryStage.setX(event.getScreenX() + xOffset);
                    primaryStage.setY(event.getScreenY() + yOffset);
                });

                VBox mainContainer = new VBox(titleBar, content);
                mainContainer.getStyleClass().add("mainContainer");
                Scene mainScene = new Scene(mainContainer);
                mainScene.setFill(Color.TRANSPARENT);
                mainScene.getStylesheets().add("styles.css");

                // Switch back to the main UI thread to update the scene
                javafx.application.Platform.runLater(() -> primaryStage.setScene(mainScene));
            } catch (IOException e) {
                e.printStackTrace(); // Handle exceptions appropriately
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }



    public static void main(String[] args) {
        launch();
    }
}

