package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.viewmodel.MainViewModel;
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
            mainViewModel.setSelectedTour(toursListView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void onTourClick(MouseEvent event) {
        Tour selectedTour = toursListView.getSelectionModel().getSelectedItem();
        mainViewModel.setSelectedTour(selectedTour);
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
}