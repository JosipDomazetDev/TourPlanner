package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.repository.api.MapQuestAPIRepository;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.data.repository.data.TourDataRepository;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class ToursViewModel {
    private final StringProperty title = new SimpleStringProperty();
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final DataRepository<Tour> tourRepository = TourDataRepository.getInstance();
    private final MapRepository<Tour> mapRepository = MapQuestAPIRepository.getInstance();

    private final TourDetailViewModel tourDetailViewModel;
    private final StringProperty errorMsg = new SimpleStringProperty();
    private final BooleanProperty isError = new SimpleBooleanProperty();
    private final BooleanProperty isLoading = new SimpleBooleanProperty();


    public ToursViewModel(TourDetailViewModel tourDetailViewModel) {
        this.tourDetailViewModel = tourDetailViewModel;
        initialize();

        tourDetailViewModel.setOnTourUpdated(() -> {
            tours.add(new Tour());
            tours.remove(tours.size() - 1);
        });

        tourDetailViewModel.setOnTourDeleted(() -> {
            tours.remove(tourDetailViewModel.getSelectedTour());

            if (tours.size() > 0) {
                setSelectedTour(tours.get(0));
            } else {
                setSelectedTour(null);
            }
        });
    }

    public SimpleObjectProperty<ObservableList<Tour>> getProperty() {
        return new SimpleObjectProperty<>(getTours());
    }

    public void addNewTour(String name, String description, String from, String to, String transportType, Runnable onApiCompletion) throws IllegalTransportTypeException {
        setLoading();
        Tour tour = new Tour(name, description, from, to, transportType);

        mapRepository.fetchApi(tour, () -> setError("Error while fetching data from MapQuest API."), () -> {
            setSuccess();
            tours.add(tour);
            tourRepository.save(tour);

            if (onApiCompletion != null) {
                onApiCompletion.run();
            }
        });
    }


    public void performSearch(String searchTerm) {
        ArrayList<Tour> toursFromDb = tourRepository.load();
        if (searchTerm == null || searchTerm.isEmpty()) {
            tours.setAll(toursFromDb);
        } else {
            tours.setAll(
                    toursFromDb
                            .stream()
                            .filter(tour -> tour.toSearchString().contains(searchTerm.toLowerCase()))
                            .toList());
            tourDetailViewModel.setSelectedTour(null);
        }
    }

    public void setError(String msg) {
        isLoading.setValue(false);
        isError.setValue(true);
        errorMsg.setValue(msg);
    }

    public void setLoading() {
        isLoading.setValue(true);
        isError.setValue(false);
        errorMsg.setValue("");
    }

    public void setSuccess() {
        isLoading.setValue(false);
        isError.setValue(false);
        errorMsg.setValue("");
    }

    private void initialize() {
        ArrayList<Tour> tours1 = tourRepository.load();
        tours.addAll(tours1);
    }

    public void setSelectedTour(Tour selectedTour) {
        tourDetailViewModel.setSelectedTour(selectedTour);
    }
}
