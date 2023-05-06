package com.example.tourplanner;


import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.repository.api.MapRepository;
import com.example.tourplanner.data.repository.data.DataRepository;
import com.example.tourplanner.viewmodel.TourDetailViewModel;
import javafx.application.Platform;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TourDetailViewModelTest {
    @Mock
    private DataRepository<Tour> tourRepository;

    @Mock
    private MapRepository<Tour> mapRepository;

    private TourDetailViewModel tourDetailViewModel;


    public TourDetailViewModelTest() {
    }

    @BeforeAll
    public static void beforeClass() {
        Platform.startup(() -> {
        });
        // Initializes the JavaFX Toolkit before any tests are run
        // This is necessary to avoid "java.lang.IllegalStateException: Toolkit not initialized"
        // errors when the tests are run from the command line
    }


    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doAnswer(invocation -> {
            Runnable onApiCompletion = invocation.getArgument(2);
            onApiCompletion.run();
            return null;
        }).when(mapRepository).fetchApi(any(Tour.class), any(Runnable.class), any(Runnable.class));

        tourDetailViewModel = new TourDetailViewModel(tourRepository, mapRepository);
    }

    @Test
    public void testUpdateTour() throws IllegalTransportTypeException {
        Tour selectedTour = new Tour("Test Tour", "Test Description", "Test From", "Test To", "fastest", "hybrid");

        // Given
        tourDetailViewModel.setSelectedTour(null);
        tourDetailViewModel = new TourDetailViewModel(tourRepository, mapRepository);
        tourDetailViewModel.setSelectedTour(selectedTour);
        Runnable onFailure = mock(Runnable.class);

        // When
        tourDetailViewModel.getName().setValue("testNewName");
        tourDetailViewModel.getTourDescription().setValue("testNewTourDescription");
        tourDetailViewModel.getFrom().setValue("testNewFrom");
        tourDetailViewModel.getTo().setValue("testNewTo");
        tourDetailViewModel.getTransportType().setValue("fastest");
        tourDetailViewModel.getMapType().setValue("hybrid");

        tourDetailViewModel.updateTour(onFailure);

        // Then
        verify(mapRepository).fetchApi(any(Tour.class), any(), any());
        assertEquals("testNewTo", tourDetailViewModel.getTo().get());
        assertEquals("testNewFrom", tourDetailViewModel.getFrom().get());

        assertEquals("testNewTo", tourDetailViewModel.getSelectedTour().getTo());
        assertEquals("testNewFrom", tourDetailViewModel.getSelectedTour().getFrom());
    }

    @Test
    public void deleteTour() throws IllegalTransportTypeException {
        Tour selectedTour = new Tour("Test Tour", "Test Description", "Test From", "Test To", "fastest", "hybrid");
        tourDetailViewModel.setSelectedTour(selectedTour);


        Consumer<Tour> onTourDeletedMock = mock(Consumer.class);
        tourDetailViewModel.setOnTourDeleted(onTourDeletedMock);

        tourDetailViewModel.deleteTour();

        verify(tourRepository).delete(selectedTour);
        verify(onTourDeletedMock).accept(selectedTour);
    }

    @Test
    public void setTemporaryImageView() throws IllegalTransportTypeException {
        Tour selectedTour = new Tour("Test Tour", "Test Description", "Test From", "Test To", "fastest", "hybrid");
        tourDetailViewModel.setSelectedTour(selectedTour);
        selectedTour.setRouteInformation(
                ".\\images\\" + selectedTour.getImageId() + "hybrid.jpg"
        );
        tourDetailViewModel.setTemporaryImageView("satellite");

        assertEquals("file:./images/" + selectedTour.getImageId() + "satellite.jpg", tourDetailViewModel.getImageProperty().get().getUrl());
    }
}