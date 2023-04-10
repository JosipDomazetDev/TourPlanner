package com.example.tourplanner.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.util.Random;

@Getter
public class MenuViewModel {
    private final StringProperty searchString = new SimpleStringProperty();

    public void doSomething() {
        searchString.set(new Random().nextInt(50) + "");
    }

    public void performExport() {
        // TODO CADE EXPORT
        System.out.println("Export");
    }

    public void performImport() {
        // TODO CADE IMPORT
        System.out.println("Import");
    }
}
