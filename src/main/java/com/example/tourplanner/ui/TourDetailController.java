package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.ui.components.ButtonCellFactory;
import com.example.tourplanner.ui.components.converter.CustomDateStringConverter;
import com.example.tourplanner.ui.components.converter.CustomDoubleStringConverter;
import com.example.tourplanner.ui.components.converter.CustomIntegerStringConverter;
import com.example.tourplanner.viewmodel.TourDetailViewModel;
import javafx.application.Platform;
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
import java.util.Date;
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
    TableView<TourLog> logTable;
    @FXML
    private TableColumn<TourLog, Date> columnDateTime;
    @FXML
    private TableColumn<TourLog, String> columnComment;
    @FXML
    private TableColumn<TourLog, Double> columnTotalTime;
    @FXML
    private TableColumn<TourLog, Integer> columnDifficulty;
    @FXML
    private TableColumn<TourLog, Integer> columnRating;

    @FXML
    private ImageView imageView;

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

        columnDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        columnTotalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));

        columnDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        Label diff = new Label("Difficulty");
        diff.setTooltip(new Tooltip("scale from 1 to 5 (1 = easy, 5 = hard)"));
        columnDifficulty.setGraphic(diff);

        columnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        Label rat = new Label("Rating");
        rat.setTooltip(new Tooltip("scale from 1 to 5 (1 = worst, 5 = best)"));
        columnRating.setGraphic(rat);

        TableColumn<TourLog, Void> buttonColumn = new TableColumn<>("Delete");
        buttonColumn.setCellFactory(new ButtonCellFactory(tourDetailViewModel::deleteTourLog));
        logTable.getColumns().add(buttonColumn);

        makeTableEditable();
    }

    private void initializeImageView() {
        imageView.imageProperty().bindBidirectional(tourDetailViewModel.getImageProperty());
        imageView.setOnMouseClicked(event -> {
            Stage previewStage = new Stage();
            previewStage.setTitle("Image Preview");

            ImageView previewImageView = new ImageView();
            previewImageView.setImage(imageView.getImage());

            previewImageView.setFitWidth(650);
            previewImageView.setFitHeight(650);

            VBox previewBox = new VBox(previewImageView);
            previewBox.setStyle("-fx-background-color: black; -fx-alignment: center;");

            Scene previewScene = new Scene(previewBox);
            previewStage.setScene(previewScene);

            // Show the preview window
            previewStage.show();
        });
    }


    private void makeTableEditable() {
        columnDateTime.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDateStringConverter("dd.MM.yyyy HH:mm")));
        columnDateTime.setOnEditCommit(e -> {
            tourDetailViewModel.setDateTime(e.getRowValue(), e.getNewValue());
            logTable.refresh();
        });

        columnComment.setCellFactory(TextFieldTableCell.forTableColumn());
        columnComment.setOnEditCommit(e -> {
            tourDetailViewModel.setComment(e.getRowValue(), e.getNewValue());
            logTable.refresh();
        });


        columnTotalTime.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        columnTotalTime.setOnEditCommit(e -> {
            tourDetailViewModel.setTotalTime(e.getRowValue(), e.getNewValue());
            logTable.refresh();
        });

        int lowerBound = 1;
        int upperBound = 5;
        columnDifficulty.setCellFactory(TextFieldTableCell.forTableColumn((new CustomIntegerStringConverter(lowerBound, upperBound))));
        columnDifficulty.setOnEditCommit(e -> {
            tourDetailViewModel.setDifficulty(e.getRowValue(), e.getNewValue());
            logTable.refresh();
        });

        columnRating.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter(lowerBound, upperBound)));
        columnRating.setOnEditCommit(e -> {
            tourDetailViewModel.setRating(e.getRowValue(), e.getNewValue());
            logTable.refresh();
        });

        logTable.setEditable(true);
        logTable.setItems(tourDetailViewModel.getTourLogs());
    }


    public void onUpdateDetail(MouseEvent mouseEvent) {
        String name = tourDetailViewModel.getName().get();
        String tourDescription = tourDetailViewModel.getTourDescription().get();
        String from = tourDetailViewModel.getFrom().get();
        String to = tourDetailViewModel.getTo().get();
        String transportType = tourDetailViewModel.getTransportType().get();


        tourDetailViewModel.updateTour(name, tourDescription, from, to, transportType, () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Tour");
            alert.setHeaderText("Error while fetching data from MapQuest API.");
            alert.showAndWait();
        });
    }

    public void onCreateTourLogClick(MouseEvent mouseEvent) {
        tourDetailViewModel.addNewTourLog();
    }

    public void onDeleteDetail(MouseEvent mouseEvent) {
        tourDetailViewModel.deleteTour();
    }
}