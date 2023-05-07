package com.example.tourplanner.ui.components;

import com.example.tourplanner.data.model.Tour;
import javafx.scene.control.ListCell;

public class TourCell extends ListCell<Tour> {
    @Override
    protected void updateItem(Tour tour, boolean empty) {
        super.updateItem(tour, empty);
        if (empty || tour == null) {
            setText(null);
        } else {
            String name = tour.getName();
            name = name.replace("->", "→");
            name = name.replace("<-", "←");
            setText(name);
        }
    }
}