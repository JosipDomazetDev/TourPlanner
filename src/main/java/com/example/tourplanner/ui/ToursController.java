package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.viewmodel.ToursViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ToursController implements Initializable {
    @FXML
    public ListView<Tour> toursListView;


    private final ToursViewModel toursViewModel;


    public ToursController(ToursViewModel toursViewModel) {
        this.toursViewModel = toursViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toursListView.itemsProperty().bindBidirectional(toursViewModel.getProperty());

        if (toursListView.getItems().isEmpty()) {
            // Empty
        } else {
            toursListView.getSelectionModel().select(0);
            toursViewModel.setSelectedTour(toursListView.getSelectionModel().getSelectedItem());
        }

        toursViewModel.getTours().addListener((ListChangeListener<Tour>) c -> {
            // Refresh the ListView when the list changes
            // This is needed for the tour update to be reflected immediately
            toursListView.refresh();
        });
    }

    @FXML
    private void onTourClick(MouseEvent event) {
        Tour selectedTour = toursListView.getSelectionModel().getSelectedItem();
        toursViewModel.setSelectedTour(selectedTour);
    }

    public void onAddTour(MouseEvent mouseEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLDependencyInjection.load("create-tour.fxml", Locale.ENGLISH);
            Scene scene = new Scene(root);
            stage.setTitle("Create Tour");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // TODO CADE HIER FÜR DICH
    public void printSummaryReport(MouseEvent mouseEvent) {
        System.out.println(toursViewModel.getTours());
        // KA OBS BESSER IST ES IM CONTROLLER ODER VIEWMODEL ZU MACHEN
    }

    public void printTourReport(MouseEvent mouseEvent) {
        System.out.println(toursViewModel.getTourDetailViewModel().getSelectedTour());
        // KA OBS BESSER IST ES IM CONTROLLER ODER VIEWMODEL ZU MACHEN
    }
}