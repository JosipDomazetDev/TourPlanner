package com.example.tourplanner.ui;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.viewmodel.ToursViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateTourController implements Initializable {

    private final ToursViewModel toursViewModel;

    @FXML
    private TextField tourName;

    @FXML
    private TextField tourDescription;

    @FXML
    private TextField from;

    @FXML
    private TextField to;

    @FXML
    private ChoiceBox<String> transportType;

    @FXML
    private ChoiceBox<String> mapType;

    @FXML
    private Label errorLabel;

    @FXML
    private ProgressIndicator progressIndicator;


    public CreateTourController(ToursViewModel toursViewModel) {
        this.toursViewModel = toursViewModel;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.textProperty().bindBidirectional(toursViewModel.getErrorMsg());
        errorLabel.visibleProperty().bindBidirectional(toursViewModel.getIsError());
        progressIndicator.visibleProperty().bindBidirectional(toursViewModel.getIsLoading());
        toursViewModel.setSuccess();
    }


    public void onCreateTourClick(MouseEvent mouseEvent) {
        String name = tourName.getText();
        String description = tourDescription.getText();
        String from = this.from.getText();
        String to = this.to.getText();
        String transportType = this.transportType.getValue();
        String mapType = this.mapType.getValue();

        if (name.isEmpty() || description.isEmpty() || from.isEmpty() || to.isEmpty() || transportType.isEmpty()) {
            showError("Please fill out all fields!");
            return;
        }

        try {
            toursViewModel.addNewTour(name, description, from, to, transportType,mapType, () -> {
                Stage window = (Stage) (tourName.getScene().getWindow());
                window.close();
            });
        } catch (IllegalTransportTypeException e) {
            showError("Illegal transport type. Please use one of the following: " + String.join(", ", Tour.validTransportTypes));
        }
    }

    private void showError(String msg) {
        toursViewModel.setError(msg);
    }
}