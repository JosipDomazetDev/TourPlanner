package com.example.tourplanner.data.repository.api;

public interface MapRepository<T> {
    void fetchApi(T selectedTour, Runnable onFailure, Runnable onTourUpdated);
}
