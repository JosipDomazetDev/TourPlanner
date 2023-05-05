package com.example.tourplanner.utils;

import javafx.scene.control.Alert;

public class PopupUtility {
    public static void createInfoPopup(String title, String text) {
        createPopup(Alert.AlertType.INFORMATION, title, text);
    }

    public static void createErrorPopup(String title, String text) {
        createPopup(Alert.AlertType.ERROR, title, text);
    }

    private static void createPopup(Alert.AlertType error, String title, String text) {
        Alert alert = new Alert(error);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
