package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.repository.Repository;
import com.example.tourplanner.data.model.repository.TourRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class ToursViewModel {
    private final StringProperty title = new SimpleStringProperty();
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final Repository<Tour> tourRepository = TourRepository.getInstance();
    private final TourDetailViewModel tourDetailViewModel;

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

    public boolean addNewTour(String name, String description, String from, String to, String transportType, double distance, String time) {
        Tour tour = new Tour(name, description, from, to, transportType, distance, time);
        tourRepository.save(tour);
        return tours.add(tour);
    }

    private void initialize() {
        ArrayList<Tour> tours1 = tourRepository.load();
        tours.addAll(tours1);
    }

    public void setSelectedTour(Tour selectedTour) {
        tourDetailViewModel.setSelectedTour(selectedTour);
    }
}
