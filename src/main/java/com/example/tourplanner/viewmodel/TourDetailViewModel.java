package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.model.repository.Repository;
import com.example.tourplanner.data.model.repository.TourLogRepository;
import com.example.tourplanner.data.model.repository.TourRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class TourDetailViewModel {
    private final Repository<TourLog> tourLogRepository = TourLogRepository.getInstance();
    private final Repository<Tour> tourRepository = TourRepository.getInstance();
    @Setter
    private Runnable onTourUpdated;
    @Setter
    private Runnable onTourDeleted;

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty tourDescription = new SimpleStringProperty();
    private final StringProperty from = new SimpleStringProperty();
    private final StringProperty to = new SimpleStringProperty();
    private final StringProperty transportType = new SimpleStringProperty();
    private final StringProperty tourDistance = new SimpleStringProperty();
    private final StringProperty estimatedTime = new SimpleStringProperty();
    private final StringProperty routeInformation = new SimpleStringProperty();

    private Tour selectedTour;
    ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();

    public TourDetailViewModel() {
    }


    public void setSelectedTour(Tour selectedTour) {
        this.selectedTour = selectedTour;
        setFields();
    }

    private void clearViewModel() {
        name.setValue("");
        tourDescription.setValue("");
        from.setValue("");
        to.setValue("");
        transportType.setValue("");
        tourDistance.setValue("");
        estimatedTime.setValue("");
        routeInformation.setValue("");

        tourLogs.clear();
    }

    private void refreshTourLogs() {
        tourLogs.clear();
        tourLogs.addAll(selectedTour.getTourLogs());
    }

    public void setFields() {
        if (selectedTour == null) {
            clearViewModel();
            return;
        }

        name.setValue(selectedTour.getName());
        tourDescription.setValue(selectedTour.getTourDescription());
        from.setValue(selectedTour.getFrom());
        to.setValue(selectedTour.getTo());
        transportType.setValue(selectedTour.getTransportType());
        tourDistance.setValue(selectedTour.getTourDistance() + "");
        estimatedTime.setValue(selectedTour.getEstimatedTime());
        routeInformation.setValue(selectedTour.getRouteInformation());

        refreshTourLogs();
    }

    public void addNewTourLog() {
        if (selectedTour == null) return;

        TourLog tourLog = new TourLog(new Date(), "", 0, 0, 0, selectedTour);
        tourLogRepository.save(tourLog);

        refreshTourLogs();
    }

    public void updateTour(String name, String tourDescription, String from, String to, String transportType, String tourDistance, String estimatedTime, String routeInformation) {
        if (selectedTour == null) return;

        selectedTour.setName(name);
        selectedTour.setTourDescription(tourDescription);
        selectedTour.setFrom(from);
        selectedTour.setTo(to);
        selectedTour.setTransportType(transportType);
        selectedTour.setTourDistance(Double.parseDouble(tourDistance));
        selectedTour.setEstimatedTime(estimatedTime);
        selectedTour.setRouteInformation(routeInformation);

        tourRepository.update(selectedTour);
        onTourUpdated.run();
    }

    public void updateTourLog(TourLog tourLog) {
        if (selectedTour == null) return;

        tourLogRepository.update(tourLog);
    }

    public void deleteTour() {
        if (selectedTour == null) return;

        tourRepository.delete(selectedTour);
        onTourDeleted.run();
    }

    public void deleteTourLog(TourLog tourLog) {
        if (selectedTour == null) return;

        selectedTour.getTourLogs().remove(tourLog);
        tourLogRepository.delete(tourLog);
        refreshTourLogs();
    }
}
