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


        toursViewModel.setOnTourSelected(tour -> {
            tourDetailViewModel.setSelectedTour(tour);
            tourLogViewModel.setSelectedTour(tour);
        });

        // Manually select first tour or nothing
        // calling this in the initialize method won't work because the VMS are not wired at that point
        toursViewModel.selectFirstTourOrNothing();

        // TourDetailViewModel -> ToursViewModel
        tourDetailViewModel.setOnTourUpdated(toursViewModel::refresh);
        tourDetailViewModel.setOnTourDeleted(toursViewModel::deleteTourFromList);

        // TourLogViewModel -> TourDetailViewModel (refresh for computed tour properties)
        tourLogViewModel.setOnRefresh(tourDetailViewModel::refresh);

        // MenuViewModel -> ToursViewModel
        menuViewModel.setOnImported(toursViewModel::load);
    }
}
