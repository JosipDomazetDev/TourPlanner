package com.example.tourplanner.viewmodel;

import com.example.tourplanner.configuration.ConfigurationReader;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.model.repository.Repository;
import com.example.tourplanner.data.model.repository.TourLogRepository;
import com.example.tourplanner.data.model.repository.TourRepository;
import com.example.tourplanner.data.model.repository.api.MapAPIFetcher;
import com.example.tourplanner.data.model.repository.api.MapQuestAPIFetcher;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.function.Consumer;

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
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty isVisible = new SimpleBooleanProperty(true);
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
        imageProperty.set(null);

        tourLogs.clear();
    }

    private void refreshTourLogs() {
        tourLogs.clear();
        tourLogs.addAll(selectedTour.getTourLogs());
    }

    public void setFields() {
        if (selectedTour == null) {
            clearViewModel();
            isVisible.set(false);
            return;
        }
        isVisible.set(true);

        name.setValue(selectedTour.getName());
        tourDescription.setValue(selectedTour.getTourDescription());
        from.setValue(selectedTour.getFrom());
        to.setValue(selectedTour.getTo());
        transportType.setValue(selectedTour.getTransportType());
        tourDistance.setValue(selectedTour.getTourDistance() + "");
        estimatedTime.setValue(selectedTour.getEstimatedTime() + " min");
        imageProperty.set(new Image("file:" + selectedTour.getRouteInformation()));


        refreshTourLogs();
    }

    public void addNewTourLog() {
        if (selectedTour == null) return;

        TourLog tourLog = new TourLog(new Date(), "", 0, 0, 0, selectedTour);
        tourLogRepository.save(tourLog);

        refreshTourLogs();
    }

    public void updateTour(String name, String tourDescription, String from, String to, String transportType, Runnable onFailure) {
        if (selectedTour == null) return;

        // TODO, sollten wir dann eig alles logs löschen?
        // TODO, input validation transport type

        selectedTour.setName(name);
        selectedTour.setTourDescription(tourDescription);
        selectedTour.setFrom(from);
        selectedTour.setTo(to);
        selectedTour.setTransportType(transportType);


        MapAPIFetcher mapFetcher = new MapQuestAPIFetcher(selectedTour, ConfigurationReader.getInstance().getApiKey());
        new Thread(mapFetcher).start();

        mapFetcher.setOnFailure(() -> {
            Platform.runLater(onFailure);
            Platform.runLater(this::setFields);
        });

        mapFetcher.setOnSuccess(() -> {
            tourRepository.update(selectedTour);
            Platform.runLater(this::setFields);
            onTourUpdated.run();
        });
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

    public <R> void updateTourLog(R newValue, Consumer<R> updateMethod) {
        if (newValue == null) {
            return;
        }

        updateMethod.accept(newValue);
        tourRepository.update(selectedTour);
    }

    public void setDateTime(TourLog tourLog, Date newValue) {
        updateTourLog(newValue, tourLog::setDateTime);
    }

    public void setComment(TourLog tourLog, String newValue) {
        updateTourLog(newValue, tourLog::setComment);
    }

    public void setTotalTime(TourLog tourLog, Double newValue) {
        updateTourLog(newValue, tourLog::setTotalTime);
    }

    public void setDifficulty(TourLog tourLog, Integer newValue) {
        updateTourLog(newValue, tourLog::setDifficulty);
    }

    public void setRating(TourLog tourLog, Integer newValue) {
        updateTourLog(newValue, tourLog::setRating);
    }

}
