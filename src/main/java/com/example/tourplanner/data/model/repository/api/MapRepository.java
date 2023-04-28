package com.example.tourplanner.data.model.repository.api;

import com.example.tourplanner.data.model.Tour;

public interface MapRepository<T> {
    void fetchApi(T selectedTour, Runnable onFailure, Runnable onTourUpdated);
}
