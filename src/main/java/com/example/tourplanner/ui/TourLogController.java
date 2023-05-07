package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.ui.components.ButtonCellFactory;
import com.example.tourplanner.ui.components.converter.CustomDateStringConverter;
import com.example.tourplanner.ui.components.converter.CustomDoubleStringConverter;
import com.example.tourplanner.ui.components.converter.CustomIntegerStringConverter;
import com.example.tourplanner.viewmodel.TourDetailViewModel;
import com.example.tourplanner.viewmodel.TourLogViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class TourLogController  implements Initializable {
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

    private final TourLogViewModel tourLogViewModel;

    public TourLogController(TourLogViewModel tourLogViewModel) {
        this.tourLogViewModel = tourLogViewModel;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));

        columnTotalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
        Label duration = new Label("Duration");
        duration.setTooltip(new Tooltip("tour duration in minutes"));
        columnTotalTime.setGraphic(duration);

        columnDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        Label diff = new Label("Difficulty");
        diff.setTooltip(new Tooltip("scale from 1 to 5 (1 = easy, 5 = hard)"));
        columnDifficulty.setGraphic(diff);

        columnRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        Label rat = new Label("Rating");
        rat.setTooltip(new Tooltip("scale from 1 to 5 (1 = worst, 5 = best)"));
        columnRating.setGraphic(rat);

        TableColumn<TourLog, Void> buttonColumn = new TableColumn<>("Action");
        buttonColumn.setCellFactory(new ButtonCellFactory(tourLogViewModel::deleteTourLog));
        logTable.getColumns().add(buttonColumn);

        makeTableEditable();
    }

    private void makeTableEditable() {
        columnDateTime.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDateStringConverter("dd.MM.yyyy HH:mm")));
        columnDateTime.setOnEditCommit(e -> {
            tourLogViewModel.setDateTime(e.getRowValue(), e.getNewValue());
            tourLogViewModel.refresh();
        });

        columnComment.setCellFactory(TextFieldTableCell.forTableColumn());
        columnComment.setOnEditCommit(e -> {
            tourLogViewModel.setComment(e.getRowValue(), e.getNewValue());
            tourLogViewModel.refresh();
        });


        columnTotalTime.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        columnTotalTime.setOnEditCommit(e -> {
            tourLogViewModel.setTotalTime(e.getRowValue(), e.getNewValue());
            tourLogViewModel.refresh();
        });

        int lowerBound = 1;
        int upperBound = 5;
        columnDifficulty.setCellFactory(TextFieldTableCell.forTableColumn((new CustomIntegerStringConverter(lowerBound, upperBound))));
        columnDifficulty.setOnEditCommit(e -> {
            tourLogViewModel.setDifficulty(e.getRowValue(), e.getNewValue());
            tourLogViewModel.refresh();
        });

        columnRating.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter(lowerBound, upperBound)));
        columnRating.setOnEditCommit(e -> {
            tourLogViewModel.setRating(e.getRowValue(), e.getNewValue());
            tourLogViewModel.refresh();
        });

        logTable.setEditable(true);
        logTable.setItems(tourLogViewModel.getTourLogs());
    }


    public void onCreateTourLogClick(MouseEvent mouseEvent) {
        tourLogViewModel.addNewTourLog();
    }


}
