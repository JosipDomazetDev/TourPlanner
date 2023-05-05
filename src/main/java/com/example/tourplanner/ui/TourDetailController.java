package com.example.tourplanner.ui;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.ui.components.ButtonCellFactory;
import com.example.tourplanner.ui.components.converter.CustomDateStringConverter;
import com.example.tourplanner.ui.components.converter.CustomDoubleStringConverter;
import com.example.tourplanner.ui.components.converter.CustomIntegerStringConverter;
import com.example.tourplanner.utils.PopupUtility;
import com.example.tourplanner.viewmodel.TourDetailViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


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
    private Label tourDistanceTourDetailTextField;

    @FXML
    private Label estimatedTimeTourDetailTextField;

    @FXML
    private Label popularityTourDetailTextField;

    @FXML
    private Label childFriendlinessTourDetailTextField;


    @FXML
    private ImageView imageView;

    @FXML
    private VBox detailVbox;

    @FXML
    private VBox nothingSelectedVbox;

    public TourDetailController(TourDetailViewModel tourDetailViewModel) {
        this.tourDetailViewModel = tourDetailViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeImageView();

        nameTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getName());
        tourDescriptionTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getTourDescription());
        fromTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getFrom());
        toTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getTo());
        transportTypeTourDetailTextField.textProperty().bindBidirectional(tourDetailViewModel.getTransportType());
        tourDistanceTourDetailTextField.textProperty().bind(tourDetailViewModel.getTourDistance());
        estimatedTimeTourDetailTextField.textProperty().bind(tourDetailViewModel.getEstimatedTime());
        popularityTourDetailTextField.textProperty().bind(tourDetailViewModel.getPopularity());
        childFriendlinessTourDetailTextField.textProperty().bind(tourDetailViewModel.getChildFriendliness());

        detailVbox.visibleProperty().bindBidirectional(tourDetailViewModel.getIsVisible());
        nothingSelectedVbox.visibleProperty().bind(Bindings.not(tourDetailViewModel.getIsVisible()));
        nothingSelectedVbox.managedProperty().bind(Bindings.not(tourDetailViewModel.getIsVisible()));
    }

    private void initializeImageView() {
        imageView.imageProperty().bindBidirectional(tourDetailViewModel.getImageProperty());
        imageView.setOnMouseClicked(event -> {
            Stage previewStage = new Stage();
            previewStage.setTitle("Image Preview");

            ImageView previewImageView = new ImageView();
            previewImageView.setImage(imageView.getImage());

            previewImageView.setFitWidth(750);
            previewImageView.setFitHeight(600);

            VBox previewBox = new VBox(previewImageView);
            previewBox.setStyle("-fx-background-color: black; -fx-alignment: center;");

            Scene previewScene = new Scene(previewBox);
            previewStage.setScene(previewScene);

            // Show the preview window
            previewStage.show();
        });
    }


    public void onUpdateDetail(MouseEvent mouseEvent) {
        String name = tourDetailViewModel.getName().get();
        String tourDescription = tourDetailViewModel.getTourDescription().get();
        String from = tourDetailViewModel.getFrom().get();
        String to = tourDetailViewModel.getTo().get();
        String transportType = tourDetailViewModel.getTransportType().get();


        try {
            tourDetailViewModel.updateTour(name, tourDescription, from, to, transportType, () -> {
                PopupUtility.createErrorPopup("Update Tour", "Error while updating tour.");
            });
        } catch (IllegalTransportTypeException e) {
            PopupUtility.createErrorPopup("Illegal Transport Type", "Illegal transport type. Please use one of the following: " + String.join(", ", Tour.validTransportTypes));
        }
    }


    public void onDeleteDetail(MouseEvent mouseEvent) {
        tourDetailViewModel.deleteTour();
    }
}