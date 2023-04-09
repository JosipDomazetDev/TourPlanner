package com.example.tourplanner.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.util.Random;

@Getter
public class MainViewModel {
    private final StringProperty title = new SimpleStringProperty();

    public void doSomething() {
        title.set(new Random().nextInt(50) + "");
    }
}
