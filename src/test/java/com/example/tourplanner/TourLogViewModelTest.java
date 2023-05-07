package com.example.tourplanner;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.viewmodel.TourLogViewModel;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestSetup.class)
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
        // Arrange
        Tour newSelectedTour = new Tour("Tour 2");
        TourLog newTourLog = new TourLog(new Date(), "Comment", 5, 2, 3, newSelectedTour);

        // Act
        tourLogViewModel.setSelectedTour(null);
        tourLogViewModel.setSelectedTour(newSelectedTour);

        // Assert
        ObservableList<TourLog> tourLogs = tourLogViewModel.getTourLogs();
        assertEquals(1, tourLogs.size());
        assertEquals(newTourLog, tourLogs.get(0));
    }

    @Test
    void testAddNewTourLog() {
        // Arrange
        tourLogViewModel.setSelectedTour(new Tour("Tour 2"));

        // Act
        tourLogViewModel.addNewTourLog();
        tourLogViewModel.addNewTourLog();

        // Assert
        ObservableList<TourLog> tourLogs = tourLogViewModel.getTourLogs();
        assertEquals(2, tourLogs.size());

        verify(tourLogDataRepository, times(2)).save(any(TourLog.class));
    }


    @Test
    void testDeleteTourLog() {
        // Arrange
        tourLogViewModel.setSelectedTour(new Tour("Tour 2"));
        tourLogViewModel.addNewTourLog();
        TourLog tourLog = tourLogViewModel.getTourLogs().get(0);

        // Act
        tourLogViewModel.deleteTourLog(tourLog);

        // Assert
        ObservableList<TourLog> tourLogs = tourLogViewModel.getTourLogs();
        assertEquals(0, tourLogs.size());

        verify(tourLogDataRepository, times(1)).delete(tourLog);
    }

    @Test
    void testUpdateTourLog() throws ParseException, IllegalTransportTypeException {
        // Arrange
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

        // Act
        tourLogViewModel.setComment(tourLog, newComment);
        tourLogViewModel.setRating(tourLog, newRating);
        tourLogViewModel.setDifficulty(tourLog, newDifficulty);
        tourLogViewModel.setDateTime(tourLog, newDate);
        tourLogViewModel.setTotalTime(tourLog, newTotalTime);
        // Calculated attributes
        tourLog.getTour().calculateDerivedFields();

        // Assert
        assertEquals(newTotalTime, tourLog.getTotalTime());
        assertEquals(newComment, tourLog.getComment());
        assertEquals(newRating, tourLog.getRating());
        assertEquals(newDifficulty, tourLog.getDifficulty());
        assertEquals(newDate, tourLog.getDateTime());
        verify(tourLogDataRepository, times(5)).update(tourLog);

        assertEquals(37, tourLog.getTour().getChildFriendliness());
        assertEquals(10, tourLog.getTour().getPopularity());
    }

    @Test
    void testClearViewModel() {
        // Act
        tourLogViewModel.clearViewModel();

        // Assert
        ObservableList<TourLog> tourLogs = tourLogViewModel.getTourLogs();
        assertEquals(0, tourLogs.size());
    }
}
