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
import java.util.Random;

@Getter
public class MainViewModel {
    private final StringProperty title = new SimpleStringProperty();
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final Repository<Tour> tourRepository = new TourRepository();


    public MainViewModel() {
        initialize();
    }

    public void doSomething() {
        title.set(new Random().nextInt(50) + "");
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
}
