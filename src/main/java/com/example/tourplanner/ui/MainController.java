package com.example.tourplanner.ui;

import com.example.tourplanner.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label welcomeText;
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
//        welcomeText.textProperty().bindBidirectional(mainViewModel.getTitle());
    }
}