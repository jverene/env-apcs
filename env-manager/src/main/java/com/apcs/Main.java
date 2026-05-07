package com.apcs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        FontIcon icon = new FontIcon("mdi-home");
        icon.setIconSize(48);

        Label label = new Label("Hello world!");
        
        VBox root = new VBox(10, icon, label);
        root.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("JavaFX Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}