package com.example.tourplanner.viewmodel;

public class MainViewModel {
    private final ToursViewModel toursViewModel;
    private final MenuViewModel menuViewModel;
    private final TourDetailViewModel tourDetailViewModel;
    private final TourLogViewModel tourLogViewModel;

    public MainViewModel(ToursViewModel toursViewModel, MenuViewModel menuViewModel, TourDetailViewModel tourDetailViewModel, TourLogViewModel tourLogViewModel) {
        this.toursViewModel = toursViewModel;
        this.menuViewModel = menuViewModel;
        this.tourDetailViewModel = tourDetailViewModel;
        this.tourLogViewModel = tourLogViewModel;

        tourDetailViewModel.setOnTourSelected(tourLogViewModel::setSelectedTour);
        tourDetailViewModel.setOnClearViewModel(tourLogViewModel::clearViewModel);
        tourDetailViewModel.setOnRefresh(tourLogViewModel::refresh);
    }
}
