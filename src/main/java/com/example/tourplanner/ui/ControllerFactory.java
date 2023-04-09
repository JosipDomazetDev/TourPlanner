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

    public ControllerFactory() {
        this.mainViewModel = new MainViewModel();
    }

    public Object create(Class<?> controllerClass) {
        if (controllerClass == MainController.class) {
            return new MainController(mainViewModel);
        }

        throw new IllegalArgumentException("Unknown: " + controllerClass);
    }
}
