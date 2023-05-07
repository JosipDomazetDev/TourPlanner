package com.example.tourplanner;


import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.data.repository.data.MassDataRepository;
import com.example.tourplanner.data.repository.fs.FileRepository;
import com.example.tourplanner.viewmodel.*;
import javafx.application.Platform;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * This class is used to test the MainViewModel class. It makes sure the VMs are wired correctly.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestSetup.class)
public class MainViewModelTest {


    @Mock
    private MassDataRepository<Tour> tourRepository;
    @Mock
    private DataRepository<TourLog> tourLogRepository;
    @Mock
    private MapRepository<Tour> mapRepository;
    @Mock
    private FileRepository<Tour> fileRepository;

    private ToursViewModel toursViewModel;
    private MenuViewModel menuViewModel;
    private TourDetailViewModel tourDetailViewModel;
    private TourLogViewModel tourLogViewModel;

    private MainViewModel mainViewModel;

    @BeforeAll
    public static void beforeClass() {
        // Initializes the JavaFX Toolkit before any tests are run
        // This is necessary to avoid "java.lang.IllegalStateException: Toolkit not initialized"
        // errors when the tests are run from the command line
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Ignore the exception if the platform was already started
        }
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doAnswer(invocation -> {
            Runnable onApiCompletion = invocation.getArgument(2);
            onApiCompletion.run();
            return null;
        }).when(mapRepository).fetchApi(any(Tour.class), any(Runnable.class), any(Runnable.class));

        this.toursViewModel = Mockito.spy(new ToursViewModel(tourRepository, mapRepository));
        this.tourDetailViewModel = Mockito.spy(new TourDetailViewModel(tourRepository, mapRepository));
        this.tourLogViewModel = Mockito.spy(new TourLogViewModel(tourLogRepository));
        this.menuViewModel = Mockito.spy(new MenuViewModel(tourRepository, fileRepository));

        mainViewModel = new MainViewModel(toursViewModel, menuViewModel, tourDetailViewModel, tourLogViewModel);

        Mockito.reset(toursViewModel, tourDetailViewModel, tourLogViewModel, menuViewModel);
    }

    @Test
    public void testTourSelection() {
        toursViewModel.setSelectedTour(null);

        verify(tourDetailViewModel).setSelectedTour(null);
        verify(tourLogViewModel).setSelectedTour(null);
    }

    @Test
    public void testOnTourUpdated() throws IllegalTransportTypeException {
        Tour selectedTour = new Tour("Test Tour", "Test Description", "Test From", "Test To", "fastest", "hybrid");
        tourDetailViewModel.setSelectedTour(selectedTour);
        tourDetailViewModel.updateTour(null);

        verify(toursViewModel).refresh();
    }

    @Test
    public void testOnTourDeleted() throws IllegalTransportTypeException {
        Tour selectedTour = new Tour("Test Tour", "Test Description", "Test From", "Test To", "fastest", "hybrid");
        tourDetailViewModel.setSelectedTour(selectedTour);
        tourDetailViewModel.deleteTour();

        verify(toursViewModel).deleteTourFromList(selectedTour);
    }

    @Test
    public void testOnRefresh() throws IllegalTransportTypeException {
        Tour selectedTour = new Tour("Test Tour", "Test Description", "Test From", "Test To", "fastest", "hybrid");
        tourDetailViewModel.setSelectedTour(selectedTour);
        tourLogViewModel.refresh();

        verify(tourDetailViewModel).refresh();
    }

    @Test
    public void testOnImported() {
        menuViewModel.performImport("", tours -> {
        }, null);

        verify(toursViewModel).load();
    }
}
