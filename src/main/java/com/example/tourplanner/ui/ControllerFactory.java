package com.example.tourplanner.ui;

import com.example.tourplanner.ui.MainController;
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

    public ControllerFactory() {
        this.mainViewModel = new MainViewModel();
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

        throw new IllegalArgumentException("Unknown: " + controllerClass);
    }
}
