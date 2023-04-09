package com.example.tourplanner.ui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Locale;


public class FXMLDependencyInjection {

        public static Parent load(String fxml, Locale locale) throws IOException {
            FXMLLoader loader = getLoader(fxml, locale);
            return loader.load();
        }

        private static FXMLLoader getLoader(String location, Locale locale) {
            return new FXMLLoader(
                    FXMLDependencyInjection.class.getResource("/com/example/tourplanner/" + location),
                    null,
                    new JavaFXBuilderFactory(),
                    controllerClass -> ControllerFactory.getInstance().create(controllerClass)
            );
        }
    }
