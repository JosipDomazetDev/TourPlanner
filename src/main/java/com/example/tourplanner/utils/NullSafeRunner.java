package com.example.tourplanner.utils;

import com.example.tourplanner.data.model.Tour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class NullSafeRunner {
    private static final Logger logger = LogManager.getLogger(NullSafeRunner.class);

    public static void run(Runnable runnable) {
        if (runnable == null) {
            logger.warn("Runnable is null, nothing happened");
            return;
        }

        runnable.run();
    }

    public static void accept(Consumer<Tour> runnable, Tour selectedTour) {
        if (runnable == null) {
            logger.warn("Consumer is null, nothing happened");
            return;
        }

        runnable.accept(selectedTour);
    }
}
