package com.example.tourplanner;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.viewmodel.TourLogViewModel;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TourLogViewModelTest {

    private TourLogViewModel tourLogViewModel;

    @Mock
    private DataRepository<TourLog> tourLogDataRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tourLogViewModel = new TourLogViewModel(tourLogDataRepository);
    }

    @Test
    void testSetSelectedTour() {
        // Given
        Tour newSelectedTour = new Tour("Tour 2");
        TourLog newTourLog = new TourLog(new Date(), "Comment", 5, 2, 3, newSelectedTour);

        // When
        tourLogViewModel.setSelectedTour(null);
        tourLogViewModel.setSelectedTour(newSelectedTour);

        // Then
        ObservableList<TourLog> tourLogs = tourLogViewModel.getTourLogs();
        assertEquals(1, tourLogs.size());
        assertEquals(newTourLog, tourLogs.get(0));
    }

    @Test
    void testAddNewTourLog() {
        // When
        tourLogViewModel.setSelectedTour(new Tour("Tour 2"));
        tourLogViewModel.addNewTourLog();
        tourLogViewModel.addNewTourLog();

        // Then
        ObservableList<TourLog> tourLogs = tourLogViewModel.getTourLogs();
        assertEquals(2, tourLogs.size());

        verify(tourLogDataRepository, times(2)).save(any(TourLog.class));
    }


    @Test
    void testDeleteTourLog() {
        tourLogViewModel.setSelectedTour(new Tour("Tour 2"));
        tourLogViewModel.addNewTourLog();
        TourLog tourLog = tourLogViewModel.getTourLogs().get(0);

        // When
        tourLogViewModel.deleteTourLog(tourLog);

        // Then
        ObservableList<TourLog> tourLogs = tourLogViewModel.getTourLogs();
        assertEquals(0, tourLogs.size());

        verify(tourLogDataRepository, times(1)).delete(tourLog);
    }

    @Test
    void testUpdateTourLog() throws ParseException, IllegalTransportTypeException {
        // Given
        String newComment = "New Comment";
        int newRating = 4;
        int newDifficulty = 4;
        Date newDate = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2021");
        Double newTotalTime = 40.0;

        Tour selectedTour = new Tour("Mount Everest Base Camp Trek",
                "A trek to the base of the highest mountain in the world.",
                20.0,
                "Lukla",
                "Mount Everest Base Camp",
                "walk",
                "hybrid",
                30,
                "",
                5,
                3);

        tourLogViewModel.setSelectedTour(selectedTour);
        tourLogViewModel.addNewTourLog();
        TourLog tourLog = tourLogViewModel.getTourLogs().get(0);

        // When
        tourLogViewModel.setComment(tourLog, newComment);
        tourLogViewModel.setRating(tourLog, newRating);
        tourLogViewModel.setDifficulty(tourLog, newDifficulty);
        tourLogViewModel.setDateTime(tourLog, newDate);
        tourLogViewModel.setTotalTime(tourLog, newTotalTime);

        // Then
        assertEquals(newTotalTime, tourLog.getTotalTime());
        assertEquals(newComment, tourLog.getComment());
        assertEquals(newRating, tourLog.getRating());
        assertEquals(newDifficulty, tourLog.getDifficulty());
        assertEquals(newDate, tourLog.getDateTime());

        verify(tourLogDataRepository, times(5)).update(tourLog);

        // Calculated attributes
        tourLog.getTour().calculateDerivedFields();
        assertEquals(37, tourLog.getTour().getChildFriendliness());
        assertEquals(10, tourLog.getTour().getPopularity());
    }

    @Test
    void testClearViewModel() {
        // When
        tourLogViewModel.clearViewModel();

        // Then
        ObservableList<TourLog> tourLogs = tourLogViewModel.getTourLogs();
        assertEquals(0, tourLogs.size());
    }
}
