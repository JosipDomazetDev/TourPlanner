package com.example.tourplanner.data.repository.api;

import com.example.tourplanner.configuration.ConfigurationReader;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.repository.api.fetcher.MapAPIFetcher;
import com.example.tourplanner.data.repository.api.fetcher.MapQuestAPIFetcher;

public class MapQuestAPIRepository implements MapRepository<Tour> {
    @Override
    public void fetchApi(Tour selectedTour, Runnable onFailure, Runnable onTourUpdated) {
        MapAPIFetcher mapFetcher = new MapQuestAPIFetcher(selectedTour, ConfigurationReader.getInstance().getApiKey());
        new Thread(mapFetcher).start();

        mapFetcher.setOnFailure(onFailure);
        mapFetcher.setOnSuccess(onTourUpdated);
    }
}
