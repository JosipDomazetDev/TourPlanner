package com.example.tourplanner.ui;

import com.example.tourplanner.viewmodel.MenuViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    private final MenuViewModel menuViewModel;
    @FXML
    TextField searchTextField;


    public MenuController(MenuViewModel menuViewModel) {
        this.menuViewModel = menuViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        searchTextField.textProperty().bindBidirectional(menuViewModel.getSearchString());
//        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(oldValue + "/" + newValue);
//        });
    }

    @FXML
    protected void onExportClick() {
        menuViewModel.performExport();
    }

    @FXML
    public void onImportClick(ActionEvent actionEvent) {
        menuViewModel.performImport();
    }
}
