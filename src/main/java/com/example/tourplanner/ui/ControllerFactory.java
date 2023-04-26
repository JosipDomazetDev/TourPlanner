package com.example.tourplanner.ui;

import com.example.tourplanner.viewmodel.*;

public final class ControllerFactory {
    private static ControllerFactory instance;

    public static ControllerFactory getInstance() {
        if (instance == null) {
            instance = new ControllerFactory();
        }

        return instance;
    }

    private final ToursViewModel toursViewModel;
    private final MenuViewModel menuViewModel;
    private final TourDetailViewModel tourDetailViewModel;

    public ControllerFactory() {
        this.tourDetailViewModel = new TourDetailViewModel();
        this.toursViewModel = new ToursViewModel(tourDetailViewModel);

        this.menuViewModel = new MenuViewModel();
    }

    public Object create(Class<?> controllerClass) {
        if (controllerClass == ToursController.class) {
            return new ToursController(toursViewModel);
        }

        if (controllerClass == MenuController.class) {
            return new MenuController(menuViewModel);
        }


        if (controllerClass == CreateTourController.class) {
            return new CreateTourController(toursViewModel);
        }

        if (controllerClass == TourDetailController.class) {
            return new TourDetailController(tourDetailViewModel);
        }

        throw new IllegalArgumentException("Unknown: " + controllerClass);
    }
}
