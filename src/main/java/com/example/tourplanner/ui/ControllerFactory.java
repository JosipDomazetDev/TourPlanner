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

    private final MainViewModel mainViewModel;
    private final MenuViewModel menuViewModel;
    private final TourDetailViewModel tourDetailViewModel;

    public ControllerFactory() {
        this.tourDetailViewModel = new TourDetailViewModel();
        this.mainViewModel = new MainViewModel(tourDetailViewModel);

        this.menuViewModel = new MenuViewModel();
    }

    public Object create(Class<?> controllerClass) {
        if (controllerClass == MainController.class) {
            return new MainController(mainViewModel);
        }

        if (controllerClass == MenuController.class) {
            return new MenuController(menuViewModel);
        }


        if (controllerClass == CreateTourController.class) {
            return new CreateTourController(mainViewModel);
        }

        if (controllerClass == TourDetailController.class) {
            return new TourDetailController(tourDetailViewModel);
        }

        throw new IllegalArgumentException("Unknown: " + controllerClass);
    }
}
