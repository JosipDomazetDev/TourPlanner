package com.example.tourplanner.data.model.repository.api;

public interface MapAPIFetcher extends Runnable {
    void setOnSuccess(Runnable runnable);

    void setOnFailure(Runnable runnable);
}
