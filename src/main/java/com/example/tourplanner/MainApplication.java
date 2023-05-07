package com.example.tourplanner;

import com.example.tourplanner.ui.di.FXMLDependencyInjection;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLDependencyInjection.load("main-view.fxml", Locale.ENGLISH);
        Scene scene = new Scene(root, 1000, 700);

        System.setProperty("log4j.configurationFile", "./log4j2.xml");
        scene.getStylesheets().add(getClass().getResource("/css/bootsstrap.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/custom.css").toExternalForm());

        stage.setTitle("TourPlanner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}