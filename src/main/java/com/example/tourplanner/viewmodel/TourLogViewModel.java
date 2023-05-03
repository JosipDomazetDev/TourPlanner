package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.data.repository.data.TourLogDataRepository;
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
public class TourLogViewModel {

    ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();
    private static final Logger logger = LogManager.getLogger(TourLogViewModel.class.getSimpleName());
    private final DataRepository<TourLog> tourLogRepository;
    private Tour selectedTour;

    public TourLogViewModel(DataRepository<TourLog> tourLogDataRepository) {
        this.tourLogRepository = tourLogDataRepository;
    }

    public void setSelectedTour(Tour selectedTour) {
        logger.info("Selected tour: {}", selectedTour);
        this.selectedTour = selectedTour;
        refresh();
    }

    public void refresh() {
        if (selectedTour == null) {
            clearViewModel();
            return;
        }

        tourLogs.clear();
        tourLogs.addAll(selectedTour.getTourLogs());
    }


    public void deleteTourLog(TourLog tourLog) {
        if (selectedTour == null) return;

        selectedTour.getTourLogs().remove(tourLog);
        tourLogRepository.delete(tourLog);
        refresh();
    }

    public <R> void updateTourLog(R newValue, Consumer<R> updateMethod, TourLog tourLog) {
        if (newValue == null) {
            return;
        }

        updateMethod.accept(newValue);
        tourLogRepository.update(tourLog);
        refresh();
    }

    public void addNewTourLog() {
        if (selectedTour == null) return;

        TourLog tourLog = new TourLog(new Date(), "", 0, 0, 0, selectedTour);
        tourLogRepository.save(tourLog);

        refresh();
    }

    public void setDateTime(TourLog tourLog, Date newValue) {
        updateTourLog(newValue, tourLog::setDateTime, tourLog);
    }

    public void setComment(TourLog tourLog, String newValue) {
        updateTourLog(newValue, tourLog::setComment, tourLog);
    }

    public void setTotalTime(TourLog tourLog, Double newValue) {
        updateTourLog(newValue, tourLog::setTotalTime, tourLog);
    }

    public void setDifficulty(TourLog tourLog, Integer newValue) {
        updateTourLog(newValue, tourLog::setDifficulty, tourLog);
    }

    public void setRating(TourLog tourLog, Integer newValue) {
        updateTourLog(newValue, tourLog::setRating, tourLog);
    }

    public void clearViewModel() {
        tourLogs.clear();
    }
}
