package com.example.tourplanner.ui;

import com.example.tourplanner.utils.FilePicker;
import com.example.tourplanner.utils.PopupUtility;
import com.example.tourplanner.viewmodel.MenuViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class MenuController implements Initializable {
    private final MenuViewModel menuViewModel;
    @FXML
    Label tourplannerHeader;

    public MenuController(MenuViewModel menuViewModel) {
        this.menuViewModel = menuViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    private Stage getStage() {
        return (Stage) tourplannerHeader.getScene().getWindow();
    }

    @FXML
    protected void onExportClick() {
        String filePath = FilePicker.saveFilePath(getStage());
        if (filePath == null) {
            return;
        }

        menuViewModel.performExport(filePath, (tours) -> {
            PopupUtility.createInfoPopup("Export successful!", String.format("Exported %d tours to %s.", tours.size(), filePath));
        }, () -> {
            PopupUtility.createErrorPopup("Export failed", "Please contact your administrator.");
        });
    }

    @FXML
    public void onImportClick() {
        String filePath = FilePicker.selectFilePath(getStage());
        if (filePath == null) {
            return;
        }

        menuViewModel.performImport(filePath, (tours) -> {
            PopupUtility.createInfoPopup("Import successful!", String.format("Imported %d tours from %s.", tours.size(), filePath));
        }, () -> {
            PopupUtility.createErrorPopup("Import failed", "Please contact your administrator.");
        });
    }
}
