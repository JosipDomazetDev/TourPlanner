package com.example.tourplanner;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.repository.fs.JSONFileRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(TestSetup.class)
public class JSONFileRepositoryTest {
    private JSONFileRepository fileRepository;
    private final String filePath = "./test.json";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fileRepository = new JSONFileRepository();
    }

    @Order(1)
    @Test
    public void testExportToFile() throws IllegalTransportTypeException, IOException, ParseException {
        // Arrange
        List<Tour> tours = new ArrayList<>();
        Tour tour1 = new Tour("Tour1", "Desc1", 10.0, "Location1", "Location2", "fastest", "hybrid", 60, "Route1", 10, 8);
        Tour tour2 = new Tour("Tour2", "Desc2", 20.0, "Location3", "Location4", "fastest", "hybrid", 120, "Route2", 5, 6);
        new TourLog(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2021"), "comment", 5, 20.0, 5, tour1);
        tours.add(tour1);
        tours.add(tour2);

        // Act
        fileRepository.exportToFile(tours, filePath);

        // Assert
        String content = Files.readString(Paths.get(filePath));
        String json = "{\"tours\":[{\"name\":\"Tour1\",\"tourDescription\":\"Desc1\",\"tourDistance\":10.0,\"from\":\"Location1\",\"to\":\"Location2\",\"transportType\":\"fastest\",\"mapType\":\"hybrid\",\"estimatedTime\":60,\"routeInformation\":\"Route1\",\"popularity\":10,\"childFriendliness\":8,\"logs\":[{\"dateTime\":\"2020-12-31T23:00:00Z\",\"comment\":\"comment\",\"difficulty\":5,\"totalTime\":20.0,\"rating\":5}]},{\"name\":\"Tour2\",\"tourDescription\":\"Desc2\",\"tourDistance\":20.0,\"from\":\"Location3\",\"to\":\"Location4\",\"transportType\":\"fastest\",\"mapType\":\"hybrid\",\"estimatedTime\":120,\"routeInformation\":\"Route2\",\"popularity\":5,\"childFriendliness\":6,\"logs\":[]}]}";
        assertEquals(json, content);
    }

    @Order(2)
    @Test
    void testPerformImport() throws Exception, IllegalTransportTypeException {
        // Arrange
        List<Tour> expectedTours = new ArrayList<>();
        Tour tour1 = new Tour("Tour1", "Desc1", 10.0, "Location1", "Location2", "fastest", "hybrid", 60, "Route1", 10, 8);
        Tour tour2 = new Tour("Tour2", "Desc2", 20.0, "Location3", "Location4", "fastest", "hybrid", 120, "Route2", 5, 6);
        new TourLog(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2021"), "comment", 5, 20.0, 5, tour1);
        expectedTours.add(tour1);
        expectedTours.add(tour2);

        // Act
        List<Tour> importedTours = fileRepository.importFromFile(filePath);

        // Assert
        assertEquals(expectedTours.size(), importedTours.size());
        assertEquals(expectedTours, importedTours);
        assertNotEquals(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.1970"), importedTours.get(0).getTourLogs().get(0).getDateTime());
    }
}
