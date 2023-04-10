package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.model.Tour;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.Random;

@Getter
public class MainViewModel {
    private final StringProperty title = new SimpleStringProperty();
    private final ObservableList<Tour> tours =
            FXCollections.observableArrayList(
                    new Tour("daniel"),
                    new Tour("not daniel")
            );

    public void doSomething() {
        title.set(new Random().nextInt(50) + "");
    }

    public SimpleObjectProperty<ObservableList<Tour>> getProperty() {
        return new SimpleObjectProperty<>(getTours());
    }

    public void addNewTour() {
        tours.add(new Tour("Bob"));
    }
}
