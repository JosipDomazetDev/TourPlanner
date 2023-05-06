package com.example.tourplanner;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.viewmodel.TourLogViewModel;
import com.example.tourplanner.viewmodel.ToursViewModel;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
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
public class ToursViewModelTest {

    @Mock
    private DataRepository<Tour> tourRepository;
    @Mock
    private MapRepository<Tour> mapRepository;

    private ToursViewModel viewModel;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doAnswer(invocation -> {
            Runnable onApiCompletion = invocation.getArgument(2);
            onApiCompletion.run();
            return null;
        }).when(mapRepository).fetchApi(any(Tour.class), any(Runnable.class), any(Runnable.class));

        viewModel = new ToursViewModel(tourRepository, mapRepository);
    }

    @Test
    void testAddNewTour() throws IllegalTransportTypeException {
        // given
        Tour tour = new Tour("Test Tour", "Test Description", "Test From", "Test To", "fastest", "stat");

        // when
        viewModel.addNewTour(tour.getName(), tour.getTourDescription(), tour.getFrom(), tour.getTo(),
                tour.getTransportType(), tour.getMapType(), () -> {});
        viewModel.addNewTour(tour.getName(), tour.getTourDescription(), tour.getFrom(), tour.getTo(),
                tour.getTransportType(), tour.getMapType(),  () -> {});

        // then
        verify(mapRepository, times(2)).fetchApi(any(Tour.class), any(Runnable.class), any(Runnable.class));
        verify(tourRepository, times(2)).save(any(Tour.class));
        ObservableList<Tour> tours = viewModel.getTours();
        assertEquals(2, tours.size());
        assertEquals(tour, tours.get(0));
    }

    @Test
    void testPerformSearch() throws IllegalTransportTypeException {
        // given
        Tour tour1 = new Tour("Gramat-Himberg", "Test Description", "Gramatneusiedl", "Himberg", "fastest", "sattelite");
        Tour tour2 = new Tour("Gramat-Wien", "Test Description", "Gramatneusiedl", "Wien", "bicycle", "sattelite");
        ArrayList<Tour> tours = new ArrayList<>();
        tours.addAll(List.of(tour1, tour2));

        when(tourRepository.load()).thenReturn(tours);

        // when
        viewModel.performSearch("Wien");

        // then
        ObservableList<Tour> toursObsv = viewModel.getTours();
        assertEquals(1, toursObsv.size());
        assertEquals(tour2, toursObsv.get(0));

        // when
        viewModel.performSearch("himberg");

        // then
        toursObsv = viewModel.getTours();
        assertEquals(1, toursObsv.size());
        assertEquals(tour1, toursObsv.get(0));
    }

    @Test
    void testPerformSearchWithTourLog() throws IllegalTransportTypeException {
        // given
        Tour tour1 = new Tour("Gramat-Himberg", "Test Description", "Gramatneusiedl", "Himberg", "fastest", "sattelite");
        Tour tour2 = new Tour("Gramat-Wien", "Test Description", "Gramatneusiedl", "Wien", "bicycle", "sattelite");
        TourLog tourLog = new TourLog(new Date(), "log comment", 1, 1.0, 1, tour1);
        TourLog tourLog2 = new TourLog(new Date(), "log", 1, 1.0, 1, tour2);
        TourLog tourLog3 = new TourLog(new Date(), "log", 1, 1.0, 1, tour2);
        ArrayList<Tour> tours = new ArrayList<>();
        tours.addAll(List.of(tour1, tour2));

        when(tourRepository.load()).thenReturn(tours);

        // when
        viewModel.performSearch("log comment");

        // then
        ObservableList<Tour> toursObsv = viewModel.getTours();
        assertEquals(1, toursObsv.size());
        assertEquals(tour1, toursObsv.get(0));

        // when
        viewModel.performSearch("log");

        // then
        toursObsv = viewModel.getTours();
        assertEquals(2, toursObsv.size());
        assertEquals(tour1, toursObsv.get(0));
        assertEquals(tour2, toursObsv.get(1));
    }

    @Test
    void testDeleteTourFromList() throws IllegalTransportTypeException {
        // given
        ToursViewModel spyViewModel = spy(viewModel);

        Tour tour1 = new Tour("Gramat-Himberg", "Test Description", "Gramatneusiedl", "Himberg", "fastest", "sattelite");
        Tour tour2 = new Tour("Gramat-Wien", "Test Description", "Gramatneusiedl", "Wien", "bicycle", "sattelite");
        spyViewModel.getTours().addAll(tour1, tour2);
        int oldSize = spyViewModel.getTours().size();

        // when
        spyViewModel.deleteTourFromList(tour1);

        // then
        ObservableList<Tour> tours = spyViewModel.getTours();
        assertEquals(oldSize - 1, tours.size());
        assertEquals(tour2, tours.get(tours.size() - 1));

        verify(spyViewModel).selectFirstTourOrNothing();
        verify(spyViewModel).setSelectedTour(any(Tour.class));
    }

    @Test
    public void testSetError() {
        viewModel.setError("An error occurred");
        assertFalse(viewModel.getIsLoading().getValue());
        assertTrue(viewModel.getIsError().getValue());
        assertEquals("An error occurred", viewModel.getErrorMsg().getValue());
    }

    @Test
    public void testSetLoading() {
        viewModel.setLoading();
        assertTrue(viewModel.getIsLoading().getValue());
        assertFalse(viewModel.getIsError().getValue());
        assertEquals("", viewModel.getErrorMsg().getValue());
    }

    @Test
    public void testSetSuccess() {
        viewModel.setSuccess();
        assertFalse(viewModel.getIsLoading().getValue());
        assertFalse(viewModel.getIsError().getValue());
        assertEquals("", viewModel.getErrorMsg().getValue());
    }
}