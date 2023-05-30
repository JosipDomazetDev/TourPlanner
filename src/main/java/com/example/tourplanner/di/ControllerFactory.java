package com.example.tourplanner.di;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.api.MapQuestAPIRepository;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.data.repository.data.MassDataRepository;
import com.example.tourplanner.data.repository.data.TourDataRepository;
import com.example.tourplanner.data.repository.data.TourLogDataRepository;
import com.example.tourplanner.data.repository.fs.FileRepository;
import com.example.tourplanner.data.repository.fs.JSONFileRepository;
import com.example.tourplanner.data.repository.report.PDFReportRepository;
import com.example.tourplanner.data.repository.report.ReportRepository;
import com.example.tourplanner.ui.*;
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
    private final ToursViewModel toursViewModel;
    private final MenuViewModel menuViewModel;
    private final TourDetailViewModel tourDetailViewModel;
    private final TourLogViewModel tourLogViewModel;

    public ControllerFactory() {
        MassDataRepository<Tour> tourRepository = new TourDataRepository();
        DataRepository<TourLog> tourLogRepository = new TourLogDataRepository();
        MapRepository<Tour> mapRepository = new MapQuestAPIRepository();
        FileRepository<Tour> fileRepository = new JSONFileRepository();
        ReportRepository pdfReportRepository = new PDFReportRepository();

        this.toursViewModel = new ToursViewModel(tourRepository, mapRepository, pdfReportRepository);
        this.tourDetailViewModel = new TourDetailViewModel(tourRepository, mapRepository);
        this.tourLogViewModel = new TourLogViewModel(tourLogRepository);
        this.menuViewModel = new MenuViewModel(tourRepository, fileRepository);

        this.mainViewModel = new MainViewModel(toursViewModel, menuViewModel, tourDetailViewModel, tourLogViewModel);
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

        if (controllerClass == TourBigDetailController.class) {
            return new TourBigDetailController(tourDetailViewModel);
        }

        if (controllerClass == TourSmallDetailController.class) {
            return new TourSmallDetailController(tourDetailViewModel);
        }

        if (controllerClass == TourLogController.class) {
            return new TourLogController(tourLogViewModel);
        }


        throw new IllegalArgumentException("Unknown: " + controllerClass);
    }
}
