package com.example.tourplanner.utils;

import com.example.tourplanner.data.model.Tour;

import java.util.function.Consumer;

public class NullSafeRunner {
    public static void run(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public static void accept(Consumer<Tour> runnable, Tour selectedTour) {
        if (runnable != null) {
            runnable.accept(selectedTour);
        }
    }
}
