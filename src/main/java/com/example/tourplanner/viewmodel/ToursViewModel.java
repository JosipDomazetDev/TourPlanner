package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.data.repository.report.ReportRepository;
import com.example.tourplanner.utils.NullSafeRunner;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class ToursViewModel {
    private final StringProperty title = new SimpleStringProperty();
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final DataRepository<Tour> tourRepository;
    private final MapRepository<Tour> mapRepository;
    private final ReportRepository reportRepository;

    private final StringProperty errorMsg = new SimpleStringProperty();
    private final BooleanProperty isError = new SimpleBooleanProperty();
    private final BooleanProperty isLoading = new SimpleBooleanProperty();

    @Setter
    private Consumer<Tour> onTourSelected;

    public ToursViewModel(DataRepository<Tour> tourRepository, MapRepository<Tour> mapQuestAPIRepository, ReportRepository reportRepository) {
        this.tourRepository = tourRepository;
        this.mapRepository = mapQuestAPIRepository;
        this.reportRepository = reportRepository;
        load();
    }

    public void deleteTourFromList(Tour deletedTour) {
        tours.remove(deletedTour);
        selectFirstTourOrNothing();
    }

    public void refresh() {
        tours.add(new Tour());
        tours.remove(tours.size() - 1);
    }

    public void selectFirstTourOrNothing() {
        if (tours.size() > 0) {
            setSelectedTour(tours.get(0));
        } else {
            setSelectedTour(null);
        }
    }

    public SimpleObjectProperty<ObservableList<Tour>> getProperty() {
        return new SimpleObjectProperty<>(getTours());
    }

    public void addNewTour(String name, String description, String from, String to, String transportType, String mapType, Runnable onApiCompletion) throws IllegalTransportTypeException {
        setLoading();
        Tour tour = new Tour(name, description, from, to, transportType, mapType);

        mapRepository.fetchApi(tour, () -> setError("Error while fetching data from MapQuest API."), () -> {
            setSuccess();
            tours.add(tour);
            tourRepository.save(tour);

            if (onApiCompletion != null) {
                onApiCompletion.run();
            }
        });
    }


    public void performSearch(String searchTerm) {
        ArrayList<Tour> toursFromDb = tourRepository.load();
        if (searchTerm == null || searchTerm.isEmpty()) {
            tours.setAll(toursFromDb);
        } else {
            tours.setAll(
                    toursFromDb
                            .stream()
                            .filter(tour -> tour.toSearchString().contains(searchTerm.toLowerCase()))
                            .toList());
            setSelectedTour(null);
        }
    }

    public void setError(String msg) {
        isLoading.setValue(false);
        isError.setValue(true);
        errorMsg.setValue(msg);
    }

    public void setLoading() {
        isLoading.setValue(true);
        isError.setValue(false);
        errorMsg.setValue("");
    }

    public void setSuccess() {
        isLoading.setValue(false);
        isError.setValue(false);
        errorMsg.setValue("");
    }

    public void load() {
        tours.clear();
        ArrayList<Tour> tours1 = tourRepository.load();
        tours.addAll(tours1);
    }

    public void setSelectedTour(Tour selectedTour) {
        NullSafeRunner.accept(onTourSelected, selectedTour);
    }

    public void printTourReport(Tour selectedItem) throws IOException {
        reportRepository.printTourReport(selectedItem);
    }

    public void printSummaryReport(List<Tour> tours) throws IOException {
        reportRepository.printSummaryReport(tours);
    }
}
