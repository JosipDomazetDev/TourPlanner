package com.example.tourplanner;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.data.repository.report.ReportRepository;
import com.example.tourplanner.viewmodel.ToursViewModel;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestSetup.class)
public class ToursViewModelTest {

    @Mock
    private DataRepository<Tour> tourRepository;
    @Mock
    private MapRepository<Tour> mapRepository;
    @Mock
    private ReportRepository reportRepository;

    private ToursViewModel viewModel;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doAnswer(invocation -> {
            Runnable onApiCompletion = invocation.getArgument(2);
            onApiCompletion.run();
            return null;
        }).when(mapRepository).fetchApi(any(Tour.class), any(Runnable.class), any(Runnable.class));

        viewModel = new ToursViewModel(tourRepository, mapRepository, reportRepository);
    }

    @Test
    void testAddNewTour() throws IllegalTransportTypeException {
        // Arrange
        Tour tour = new Tour("Test Tour", "Test Description", "Test From", "Test To", "fastest", "stat");

        // Act
        viewModel.addNewTour(tour.getName(), tour.getTourDescription(), tour.getFrom(), tour.getTo(),
                tour.getTransportType(), tour.getMapType(), () -> {});
        viewModel.addNewTour(tour.getName(), tour.getTourDescription(), tour.getFrom(), tour.getTo(),
                tour.getTransportType(), tour.getMapType(),  () -> {});

        // Assert
        verify(mapRepository, times(2)).fetchApi(any(Tour.class), any(Runnable.class), any(Runnable.class));
        verify(tourRepository, times(2)).save(any(Tour.class));
        ObservableList<Tour> tours = viewModel.getTours();
        assertEquals(2, tours.size());
        assertEquals(tour, tours.get(0));
    }

    @Test
    void testPerformSearch() throws IllegalTransportTypeException {
        // Arrange
        Tour tour1 = new Tour("Gramat-Himberg", "Test Description", "Gramatneusiedl", "Himberg", "fastest", "sattelite");
        Tour tour2 = new Tour("Gramat-Wien", "Test Description", "Gramatneusiedl", "Wien", "bicycle", "sattelite");
        ArrayList<Tour> tours = new ArrayList<>();
        tours.addAll(List.of(tour1, tour2));

        when(tourRepository.load()).thenReturn(tours);

        // Act
        viewModel.performSearch("Wien");

        // Assert
        ObservableList<Tour> toursObservableList = viewModel.getTours();
        assertEquals(1, toursObservableList.size());
        assertEquals(tour2, toursObservableList.get(0));

        // Act
        viewModel.performSearch("himberg");

        // Assert
        toursObservableList = viewModel.getTours();
        assertEquals(1, toursObservableList.size());
        assertEquals(tour1, toursObservableList.get(0));
    }

    @Test
    void testPerformSearchWithTourLog() throws IllegalTransportTypeException {
        // Arrange
        Tour tour1 = new Tour("Gramat-Himberg", "Test Description", "Gramatneusiedl", "Himberg", "fastest", "sattelite");
        Tour tour2 = new Tour("Gramat-Wien", "Test Description", "Gramatneusiedl", "Wien", "bicycle", "sattelite");
        TourLog tourLog = new TourLog(new Date(), "log comment", 1, 1.0, 1, tour1);
        TourLog tourLog2 = new TourLog(new Date(), "log", 1, 1.0, 1, tour2);
        TourLog tourLog3 = new TourLog(new Date(), "log", 1, 1.0, 1, tour2);
        ArrayList<Tour> tours = new ArrayList<>();
        tours.addAll(List.of(tour1, tour2));

        when(tourRepository.load()).thenReturn(tours);

        // Act
        viewModel.performSearch("log comment");

        // Assert
        ObservableList<Tour> toursObservableList = viewModel.getTours();
        assertEquals(1, toursObservableList.size());
        assertEquals(tour1, toursObservableList.get(0));

        // Act
        viewModel.performSearch("log");

        // Assert
        toursObservableList = viewModel.getTours();
        assertEquals(2, toursObservableList.size());
        assertEquals(tour1, toursObservableList.get(0));
        assertEquals(tour2, toursObservableList.get(1));
    }

    @Test
    void testDeleteTourFromList() throws IllegalTransportTypeException {
        // Arrange
        ToursViewModel spyViewModel = spy(viewModel);
        Tour tour1 = new Tour("Gramat-Himberg", "Test Description", "Gramatneusiedl", "Himberg", "fastest", "sattelite");
        Tour tour2 = new Tour("Gramat-Wien", "Test Description", "Gramatneusiedl", "Wien", "bicycle", "sattelite");
        spyViewModel.getTours().addAll(tour1, tour2);

        // Act
        spyViewModel.deleteTourFromList(tour1);

        // Assert
        ObservableList<Tour> tours = spyViewModel.getTours();
        assertEquals(1, tours.size());
        assertEquals(tour2, tours.get(0));

        verify(spyViewModel).selectFirstTourOrNothing();
        verify(spyViewModel).setSelectedTour(any(Tour.class));
    }

    @Test
    public void testSetError() {
        // Act
        viewModel.setError("An error occurred");

        // Assert
        assertFalse(viewModel.getIsLoading().getValue());
        assertTrue(viewModel.getIsError().getValue());
        assertEquals("An error occurred", viewModel.getErrorMsg().getValue());
    }

    @Test
    public void testSetLoading() {
        // Act
        viewModel.setLoading();

        // Assert
        assertTrue(viewModel.getIsLoading().getValue());
        assertFalse(viewModel.getIsError().getValue());
        assertEquals("", viewModel.getErrorMsg().getValue());
    }

    @Test
    public void testSetSuccess() {
        // Act
        viewModel.setSuccess();

        // Assert
        assertFalse(viewModel.getIsLoading().getValue());
        assertFalse(viewModel.getIsError().getValue());
        assertEquals("", viewModel.getErrorMsg().getValue());
    }

    @Test
    public void testRefresh() {
        // Arrange
        ToursViewModel spyViewModel = spy(viewModel);

        // Act
        spyViewModel.selectFirstTourOrNothing();
        spyViewModel.refresh();

        // Assert
        verify(spyViewModel).setSelectedTour(null);
        assertEquals(0, spyViewModel.getTours().size());
    }
}