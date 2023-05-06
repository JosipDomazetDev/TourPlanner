package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
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

import java.io.File;
import java.util.function.Consumer;

@Getter
public class TourDetailViewModel {
    private static final Logger logger = LogManager.getLogger(TourDetailViewModel.class.getSimpleName());
    private final DataRepository<Tour> tourRepository;
    private final MapRepository<Tour> mapRepository;

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty tourDescription = new SimpleStringProperty();
    private final StringProperty from = new SimpleStringProperty();
    private final StringProperty to = new SimpleStringProperty();
    private final StringProperty transportType = new SimpleStringProperty();
    private final StringProperty mapType = new SimpleStringProperty();
    private final StringProperty tourDistance = new SimpleStringProperty();
    private final StringProperty estimatedTime = new SimpleStringProperty();
    private final StringProperty popularity = new SimpleStringProperty();
    private final StringProperty childFriendliness = new SimpleStringProperty();
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty isVisible = new SimpleBooleanProperty(true);
    private Tour selectedTour;

    @Setter
    private Runnable onTourUpdated;
    @Setter
    private Consumer<Tour> onTourDeleted;

    public TourDetailViewModel(DataRepository<Tour> tourRepository, MapRepository<Tour> mapQuestAPIRepository) {
        this.tourRepository = tourRepository;
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
        mapType.setValue("");
        tourDistance.setValue("");
        estimatedTime.setValue("");
        popularity.setValue("");
        childFriendliness.setValue("");
        imageProperty.set(null);
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
        mapType.setValue(selectedTour.getMapType());
        tourDistance.setValue(selectedTour.getTourDistance() + " km");
        estimatedTime.setValue(selectedTour.getEstimatedTime() + " min");
        popularity.setValue(selectedTour.getPopularity() + "%");
        childFriendliness.setValue(selectedTour.getChildFriendliness() + "%");
        imageProperty.set(new Image("file:" + selectedTour.getRouteInformation()));
    }


    public void updateTour(Runnable onFailure) throws IllegalTransportTypeException {
        if (selectedTour == null) return;

        String name = getName().get();
        String tourDescription = getTourDescription().get();
        String from = getFrom().get();
        String to = getTo().get();
        String transportType = getTransportType().get();
        String mapType = getMapType().get();


        selectedTour.setName(name);
        selectedTour.setTourDescription(tourDescription);
        selectedTour.setFrom(from);
        selectedTour.setTo(to);
        selectedTour.setTransportType(transportType);
        selectedTour.setMapType(mapType);

        mapRepository.fetchApi(selectedTour, () -> {
            // Update anyway, even if the map fetch failed
            tourRepository.update(selectedTour);
            Platform.runLater(this::refresh);
            Platform.runLater(onFailure);
        }, () -> {
            tourRepository.update(selectedTour);
            Platform.runLater(this::refresh);

            NullSafeRunner.run(onTourUpdated);
        });
    }

    public void deleteTour() {
        if (selectedTour == null) return;

        tourRepository.delete(selectedTour);
        NullSafeRunner.accept(onTourDeleted, selectedTour);
    }

    public void setTemporaryImageView(String mapType) {
        // Set the routeInformation even before the new map was fetched (because the map might have been already fetched in the past)
        String routeInformation = selectedTour.getRouteInformation();
        String directoryPath = routeInformation.substring(0, routeInformation.lastIndexOf(File.separator));
        String fileType = routeInformation.substring(routeInformation.lastIndexOf(".") + 1);

        String tempRouteInformation = (directoryPath + File.separator + selectedTour.getImageFilename(mapType, fileType));
        imageProperty.set(new Image("file:" + tempRouteInformation));
    }
}
