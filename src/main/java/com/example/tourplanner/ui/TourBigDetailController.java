package com.example.tourplanner.ui;

import com.example.tourplanner.viewmodel.TourDetailViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;


public class TourBigDetailController implements Initializable {
    private final TourDetailViewModel tourDetailViewModel;

    @FXML
    private VBox detailVbox;

    @FXML
    private VBox nothingSelectedVbox;

    public TourBigDetailController(TourDetailViewModel tourDetailViewModel) {
        this.tourDetailViewModel = tourDetailViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailVbox.visibleProperty().bindBidirectional(tourDetailViewModel.getIsVisible());
        nothingSelectedVbox.visibleProperty().bind(Bindings.not(tourDetailViewModel.getIsVisible()));
        nothingSelectedVbox.managedProperty().bind(Bindings.not(tourDetailViewModel.getIsVisible()));
    }
}