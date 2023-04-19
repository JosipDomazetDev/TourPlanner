package com.example.tourplanner.ui;

import com.example.tourplanner.viewmodel.TourDetailViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

public class TourDetailController implements Initializable {
    private final TourDetailViewModel tourDetailViewModel;

    @FXML
    private TextField nameTourDetailTextField;

    @FXML
    private TextField tourDescriptionTourDetailTextField;

    @FXML
    private TextField fromTourDetailTextField;

    @FXML
    private TextField toTourDetailTextField;

    @FXML
    private TextField transportTypeTourDetailTextField;

    @FXML
    private TextField tourDistanceTourDetailTextField;

    @FXML
    private TextField estimatedTimeTourDetailTextField;

    @FXML
    private TextField routeInformationTourDetailTextField;


    public TourDetailController(TourDetailViewModel tourDetailViewModel) {
        this.tourDetailViewModel = tourDetailViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getName());
        tourDescriptionTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getTourDescription());
        fromTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getFrom());
        toTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getTo());
        transportTypeTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getTransportType());
        tourDistanceTourDetailTextField.textProperty().bind(tourDetailViewModel.getTourDistance());
        estimatedTimeTourDetailTextField.textProperty().bind(tourDetailViewModel.getEstimatedTime());
        routeInformationTourDetailTextField.textProperty().bind(tourDetailViewModel.getRouteInformation());
    }


    public void onUpdateDetail(MouseEvent mouseEvent) {
        String name = tourDetailViewModel.getName().get();
        String tourDescription = tourDetailViewModel.getTourDescription().get();
        String from = tourDetailViewModel.getFrom().get();
        String to = tourDetailViewModel.getTo().get();
        String transportType = tourDetailViewModel.getTransportType().get();
        String tourDistance = tourDetailViewModel.getTourDistance().get();
        String estimatedTime = tourDetailViewModel.getEstimatedTime().get();
        String routeInformation = tourDetailViewModel.getRouteInformation().get();

        tourDetailViewModel.updateTour(name, tourDescription, from, to, transportType, tourDistance, estimatedTime, routeInformation);
    }
}