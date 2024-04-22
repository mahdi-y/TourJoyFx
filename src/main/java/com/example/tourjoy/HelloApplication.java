package com.example.tourjoy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
import javafx.animation.FadeTransition;
import javafx.util.Duration;


public class HelloApplication extends Application {

    private static Stage primaryStage;
    private static double xOffset = 0;
    private static double yOffset = 0;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/signUp.fxml"));
        Parent root = fxmlLoader.load();

        makeStageDraggable(root);

        Scene scene = new Scene(root, 1250, 700);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("styles.css");

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), root);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();

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
        Scene loadingScene = new Scene(loadingContainer, 1200, 700);
        loadingScene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(loadingScene);

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFileName));
                Parent content = loader.load();

                HBox titleBar = createTitleBar();
                makeStageDraggable(content);

                VBox mainContainer = new VBox(content);
                mainContainer.getStyleClass().add("mainContainer");
                Scene mainScene = new Scene(mainContainer);
                mainScene.setFill(Color.TRANSPARENT);
                mainScene.getStylesheets().add("styles.css");

                javafx.application.Platform.runLater(() -> primaryStage.setScene(mainScene));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static void makeStageDraggable(Node node) {
        node.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        node.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
    }




    public static void main(String[] args) {
        launch();
    }
}

