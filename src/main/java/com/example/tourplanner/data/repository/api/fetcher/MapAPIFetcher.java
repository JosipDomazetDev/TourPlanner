package com.example.tourplanner.data.repository.api.fetcher;

public interface MapAPIFetcher extends Runnable {
    void setOnSuccess(Runnable runnable);

    void setOnFailure(Runnable runnable);
}
