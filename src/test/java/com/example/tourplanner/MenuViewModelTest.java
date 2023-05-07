package com.example.tourplanner;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.repository.data.MassDataRepository;
import com.example.tourplanner.data.repository.fs.FileRepository;
import com.example.tourplanner.viewmodel.MenuViewModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestSetup.class)
public class MenuViewModelTest {

    private MenuViewModel menuViewModel;

    @Mock
    private MassDataRepository<Tour> tourRepository;

    @Mock
    private FileRepository<Tour> fileRepository;

    @Mock
    private Consumer<List<Tour>> onSuccess;

    @Mock
    private Runnable onError;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        menuViewModel = new MenuViewModel(tourRepository, fileRepository);
    }

    @Test
    public void testPerformExportSuccess() throws IllegalTransportTypeException, IOException {
        // Arrange
        List<Tour> tours = new ArrayList<>();
        Tour tour1 = new Tour("Tour1", "Desc1", 10.0, "Location1", "Location2", "fastest", "Map1", 60, "Route1", 10, 8);
        Tour tour2 = new Tour("Tour2", "Desc2", 20.0, "Location3", "Location4", "fastest", "Map2", 120, "Route2", 5, 6);
        tours.add(tour1);
        tours.add(tour2);
        when(tourRepository.load()).thenReturn(new ArrayList<>(tours));
        String filePath = "test.txt";

        // Act
        menuViewModel.performExport(filePath, onSuccess, onError);

        // Assert
        verify(fileRepository).exportToFile(eq(tours), eq(filePath));
        verify(onSuccess).accept(ArgumentMatchers.anyList());
    }

    @Test
    public void testPerformExportFailure() throws Exception, IllegalTransportTypeException {
        // Arrange
        List<Tour> tours = new ArrayList<>();
        Tour tour1 = new Tour("Tour1", "Desc1", 10.0, "Location1", "Location2", "fastest", "Map1", 60, "Route1", 10, 8);
        Tour tour2 = new Tour("Tour2", "Desc2", 20.0, "Location3", "Location4", "fastest", "Map2", 120, "Route2", 5, 6);
        tours.add(tour1);
        tours.add(tour2);
        when(tourRepository.load()).thenReturn(new ArrayList<>(tours));

        String filePath = "test.txt";
        doAnswer(invocationOnMock -> {
            throw new IOException("Export failed!");
        }).when(fileRepository).exportToFile(eq(tours), eq(filePath));


        // Act
        menuViewModel.performExport(filePath, onSuccess, onError);

        // Assert
        verify(fileRepository).exportToFile(eq(tours), eq(filePath));
        verify(onError).run();
    }

    @Test
    void performImportSuccessfulImport() throws Exception, IllegalTransportTypeException {
        // Arrange
        String filePath = "testfile";
        List<Tour> expectedTours = new ArrayList<>();
        expectedTours.add(new Tour("Tour 1", "Description 1", 100.0, "City 1", "City 2", "fastest", "map", 120, "Route info", 10, 8));
        expectedTours.add(new Tour("Tour 2", "Description 2", 150.0, "City 2", "City 3", "fastest", "map", 180, "Route info", 15, 7));

        when(fileRepository.importFromFile(filePath)).thenReturn(expectedTours);
        doNothing().when(tourRepository).importMultiple(expectedTours);

        // Act
        menuViewModel.performImport(filePath, onSuccess, onError);

        // Assert
        verify(fileRepository).importFromFile(filePath);
        verify(tourRepository).importMultiple(expectedTours);
        verify(onSuccess).accept(expectedTours);
    }

    @Test
    void performImportFailedImport() throws Exception, IllegalTransportTypeException {
        // Arrange
        String filePath = "testfile";
        doThrow(new IllegalTransportTypeException()).when(fileRepository).importFromFile(filePath);

        // Act
        menuViewModel.performImport(filePath, tours -> {
        }, onError);

        // Assert
        verify(fileRepository).importFromFile(filePath);
        verify(tourRepository, never()).importMultiple(anyList());
        verify(onError).run();
    }
}
