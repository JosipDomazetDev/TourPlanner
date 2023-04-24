package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.viewmodel.TourDetailViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

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
        columnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        logTable.setItems(tourDetailViewModel.getTourLogs());

        makeTableEditable();
    }

    public void modifyTour(TourLog tourLog, Runnable runnable) {
        runnable.run();
        tourDetailViewModel.updateTourLog(tourLog);
    }


    private void makeTableEditable() {

        columnDateTime.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
        columnDateTime.setOnEditCommit(e -> {
            modifyTour(e.getTableView().getItems().get(e.getTablePosition().getRow()), () -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setDateTime(e.getNewValue());
            });
        });

        columnComment.setCellFactory(TextFieldTableCell.forTableColumn());
        columnComment.setOnEditCommit(e -> {
            modifyTour(e.getTableView().getItems().get(e.getTablePosition().getRow()), () -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setComment(e.getNewValue());
            });
        });

        columnTotalTime.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        columnTotalTime.setOnEditCommit(e -> {
            modifyTour(e.getTableView().getItems().get(e.getTablePosition().getRow()), () -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setTotalTime(e.getNewValue());
            });
        });

        columnDifficulty.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnDifficulty.setOnEditCommit(e -> {
            modifyTour(e.getTableView().getItems().get(e.getTablePosition().getRow()), () -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setDifficulty((e.getNewValue()));
            });
        });

        columnRating.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        columnRating.setOnEditCommit(e -> {
            modifyTour(e.getTableView().getItems().get(e.getTablePosition().getRow()), () -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setRating(e.getNewValue());
            });
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
}