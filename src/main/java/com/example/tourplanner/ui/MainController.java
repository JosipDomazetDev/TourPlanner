package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    public ListView<Tour> toursListView;


    private final MainViewModel mainViewModel;


    public MainController(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @FXML
    protected void onHelloButtonClick() {
        mainViewModel.doSomething();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toursListView.itemsProperty().bindBidirectional(mainViewModel.getProperty());

        if (toursListView.getItems().isEmpty()) {
          // Empty
        } else {
            toursListView.getSelectionModel().select(0);
            // Select in viewmodel afterwards
            // TODO
        }
    }


    public void onAddTour(MouseEvent mouseEvent) {
        mainViewModel.addNewTour();
    }
}