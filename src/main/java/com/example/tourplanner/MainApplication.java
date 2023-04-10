package com.example.tourplanner;

import com.example.tourplanner.ui.FXMLDependencyInjection;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root =  FXMLDependencyInjection.load("main-view.fxml", Locale.ENGLISH);
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}