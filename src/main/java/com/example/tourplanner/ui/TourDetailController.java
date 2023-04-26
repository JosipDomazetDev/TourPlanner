package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.ui.components.*;
import com.example.tourplanner.ui.components.converter.CustomDateStringConverter;
import com.example.tourplanner.ui.components.converter.CustomDoubleStringConverter;
import com.example.tourplanner.ui.components.converter.CustomIntegerStringConverter;
import com.example.tourplanner.viewmodel.TourDetailViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

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
    private TextField routeInformationTourDetailTextField;

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

        logTable.setItems(tourDetailViewModel.getTourLogs());

        TableColumn<TourLog, Void> buttonColumn = new TableColumn<>("Delete");
        buttonColumn.setCellFactory(new ButtonCellFactory(tourDetailViewModel::deleteTourLog));
        logTable.getColumns().add(buttonColumn);

        makeTableEditable();
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

    public void onCreateTourLogClick(MouseEvent mouseEvent) {
        tourDetailViewModel.addNewTourLog();
    }

    public void onDeleteDetail(MouseEvent mouseEvent) {
        tourDetailViewModel.deleteTour();
    }
}