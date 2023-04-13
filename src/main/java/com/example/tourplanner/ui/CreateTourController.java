package com.example.tourplanner.ui;

import com.example.tourplanner.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateTourController implements Initializable {

    private final MainViewModel mainViewModel;

    @FXML
    private TextField tourName;

    @FXML
    private TextField tourDescription;

    @FXML
    private TextField from;

    @FXML
    private TextField to;

    @FXML
    private TextField transportType;

    @FXML
    private TextField tourDistance;

    @FXML
    private TextField estimatedTime;



    public CreateTourController(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    public void onCreateTourClick(MouseEvent mouseEvent) {
        String name = tourName.getText();
        String description = tourDescription.getText();
        String from = this.from.getText();
        String to = this.to.getText();
        String transportType = this.transportType.getText();
        double distance = Double.parseDouble(tourDistance.getText());
        String time = estimatedTime.getText();

        if (mainViewModel.addNewTour(name, description, from, to, transportType, distance, time)) {
            Stage window = (Stage) (tourName.getScene().getWindow());
            window.close();
        }
    }
}