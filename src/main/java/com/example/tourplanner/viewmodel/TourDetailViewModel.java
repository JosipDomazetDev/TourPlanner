package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.utils.NullSafeRunner;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

@Getter
public class TourDetailViewModel {
    private static final Logger logger = LogManager.getLogger(TourDetailViewModel.class.getSimpleName());
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

    @Setter
    Consumer<Tour> onTourSelected;
    @Setter
    Runnable onClearViewModel;
    @Setter
    Runnable onRefresh;

    public TourDetailViewModel(DataRepository<Tour> tourRepository, DataRepository<TourLog> tourLogDataRepository, MapRepository<Tour> mapQuestAPIRepository, TourLogViewModel tourLogViewModel) {
        this.tourRepository = tourRepository;
        this.tourLogRepository = tourLogDataRepository;
        this.mapRepository = mapQuestAPIRepository;

    }


    public void setSelectedTour(Tour selectedTour) {
        logger.info("Selected tour: {}", selectedTour);
        this.selectedTour = selectedTour;

        NullSafeRunner.accept(onTourSelected, selectedTour);
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


        NullSafeRunner.run(onClearViewModel);
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


        NullSafeRunner.run(onRefresh);
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
}
