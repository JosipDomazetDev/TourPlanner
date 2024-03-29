package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.di.FXMLDependencyInjection;
import com.example.tourplanner.ui.components.TourCell;
import com.example.tourplanner.viewmodel.ToursViewModel;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class ToursController implements Initializable {
    @FXML
    public ListView<Tour> toursListView;

    @FXML
    public TextField searchTextField;

    private final ToursViewModel toursViewModel;


    public ToursController(ToursViewModel toursViewModel) {
        this.toursViewModel = toursViewModel;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toursListView.itemsProperty().bindBidirectional(toursViewModel.getProperty());
        toursListView.setCellFactory(listView -> new TourCell());

        if (!toursListView.getItems().isEmpty()) {
            toursListView.getSelectionModel().select(0);
            toursViewModel.setSelectedTour(toursListView.getSelectionModel().getSelectedItem());
        }


        toursViewModel.getTours().addListener((ListChangeListener<Tour>) c -> {
            // Refresh the ListView when the list changes
            // This is needed for the tour update to be reflected immediately
            toursListView.refresh();
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            toursViewModel.performSearch(newValue);
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
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/bootsstrap.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/custom.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void printSummaryReport(MouseEvent mouseEvent) throws FileNotFoundException {

        try{
            toursViewModel.printSummaryReport(toursViewModel.getTours());
        } catch (IOException e) {

        }
    }

    public void printTourReport(MouseEvent mouseEvent) {
        Tour selectedItem = toursListView.getSelectionModel().getSelectedItem();

        try {
            toursViewModel.printTourReport(selectedItem);
        } catch (IOException e) {
            // Display error message
        }

        //System.out.println(selectedTour);

    }
}