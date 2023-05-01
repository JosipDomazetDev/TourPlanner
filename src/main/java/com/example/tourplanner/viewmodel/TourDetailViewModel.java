package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.api.MapQuestAPIRepository;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.data.repository.data.TourLogDataRepository;
import com.example.tourplanner.data.repository.data.TourDataRepository;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.function.Consumer;

@Getter
public class TourDetailViewModel {
    private final DataRepository<TourLog> tourLogRepository;
    private final DataRepository<Tour> tourRepository;
    private final MapRepository<Tour> mapRepository;
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
    private final StringProperty popularity = new SimpleStringProperty();
    private final StringProperty childFriendliness = new SimpleStringProperty();
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty isVisible = new SimpleBooleanProperty(true);
    private Tour selectedTour;
    ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();
    private static final Logger logger = LogManager.getLogger(TourLogDataRepository.class.getSimpleName());

    public TourDetailViewModel(DataRepository<Tour> tourRepository, DataRepository<TourLog> tourLogDataRepository, MapRepository<Tour> mapQuestAPIRepository) {
        this.tourRepository = tourRepository;
        this.tourLogRepository = tourLogDataRepository;
        this.mapRepository = mapQuestAPIRepository;
    }


    public void setSelectedTour(Tour selectedTour) {
        logger.info("Selected tour: {}", selectedTour);
        this.selectedTour = selectedTour;
        refresh();
    }

    private void clearViewModel() {
        name.setValue("");
        tourDescription.setValue("");
        from.setValue("");
        to.setValue("");
        transportType.setValue("");
        tourDistance.setValue("");
        estimatedTime.setValue("");
        popularity.setValue("");
        childFriendliness.setValue("");
        imageProperty.set(null);

        tourLogs.clear();
    }


    public void refresh() {
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
        tourDistance.setValue(selectedTour.getTourDistance() + " km");
        estimatedTime.setValue(selectedTour.getEstimatedTime() + " min");
        popularity.setValue(selectedTour.getPopularity() + "%");
        childFriendliness.setValue(selectedTour.getChildFriendliness() + "%");
        imageProperty.set(new Image("file:" + selectedTour.getRouteInformation()));

        tourLogs.clear();
        tourLogs.addAll(selectedTour.getTourLogs());
    }

    public void addNewTourLog() {
        if (selectedTour == null) return;

        TourLog tourLog = new TourLog(new Date(), "", 0, 0, 0, selectedTour);
        tourLogRepository.save(tourLog);

        refresh();
    }

    public void updateTour(String name, String tourDescription, String from, String to, String transportType, Runnable onFailure) throws IllegalTransportTypeException {
        if (selectedTour == null) return;

        // TODO, sollten wir dann eig alles logs löschen?
        // TODO, input validation transport type

        selectedTour.setName(name);
        selectedTour.setTourDescription(tourDescription);
        selectedTour.setFrom(from);
        selectedTour.setTo(to);
        selectedTour.setTransportType(transportType);

        mapRepository.fetchApi(selectedTour, () -> {
            // Update anyway, even if the map fetch failed
            tourRepository.update(selectedTour);
            Platform.runLater(this::refresh);
            Platform.runLater(onFailure);
        }, () -> {
            tourRepository.update(selectedTour);
            Platform.runLater(this::refresh);
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
        refresh();
    }

    public <R> void updateTourLog(R newValue, Consumer<R> updateMethod) {
        if (newValue == null) {
            return;
        }

        updateMethod.accept(newValue);
        tourRepository.update(selectedTour);
        refresh();
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
