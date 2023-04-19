package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.model.repository.Repository;
import com.example.tourplanner.data.model.repository.TourLogRepository;
import com.example.tourplanner.data.model.repository.TourRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TourDetailViewModel {
    private final Repository<TourLog> tourLogRepository = new TourLogRepository();
    private final Repository<Tour> tourRepository = TourRepository.getInstance();
    @Setter
    private Runnable onTourUpdated;
    private Tour selectedTour;


    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty tourDescription = new SimpleStringProperty();
    private final StringProperty from = new SimpleStringProperty();
    private final StringProperty to = new SimpleStringProperty();
    private final StringProperty transportType = new SimpleStringProperty();
    private final StringProperty tourDistance = new SimpleStringProperty();
    private final StringProperty estimatedTime = new SimpleStringProperty();
    private final StringProperty routeInformation = new SimpleStringProperty();

    public TourDetailViewModel() {
        initialize();
    }


    public void setSelectedTour(Tour selectedTour) {
        this.selectedTour = selectedTour;
        setFields();
    }

    public void setFields() {
        name.setValue(selectedTour.getName());
        tourDescription.setValue(selectedTour.getTourDescription());
        from.setValue(selectedTour.getFrom());
        to.setValue(selectedTour.getTo());
        transportType.setValue(selectedTour.getTransportType());
        tourDistance.setValue(selectedTour.getTourDistance() + "");
        estimatedTime.setValue(selectedTour.getEstimatedTime());
        routeInformation.setValue(selectedTour.getRouteInformation());
    }

    public boolean addNewTourLog(String name, String description, String from, String to, String transportType, double distance, String time) {
//        TourLog tourLog = new TourLog(name, description, from, to, transportType, distance, time);
//        tourLogRepository.save(tourLog);
//
//        return selectedTour.getTourLogs().add(tourLog);
        return false;
    }

    private void initialize() {
//        ArrayList<TourLog> tourLogs = tourLogRepository.load();
//        tourLogs.addAll(tourLogs);
    }

    public void updateTour(String name, String tourDescription, String from, String to, String transportType, String tourDistance, String estimatedTime, String routeInformation) {
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
}
