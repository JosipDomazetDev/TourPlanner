package com.example.tourplanner.data.model.repository.api.fetcher;

import com.example.tourplanner.data.model.Tour;

import com.example.tourplanner.utils.ImageNameGenerator;
import javafx.concurrent.Task;
import org.apache.http.client.utils.URIBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapQuestAPIFetcher extends Task<Void> implements MapAPIFetcher {
    private final String apiKey;
    private final Tour tour;
    private final ObjectMapper objectMapper;
    private Runnable onFailure;

    public MapQuestAPIFetcher(Tour tour, String apiKey) {
        this.tour = tour;
        this.apiKey = apiKey;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected Void call() {
        if (isCancelled()) {
            onFailure.run();
            return null;
        }

        try {
            MapQuestResponse mapQuestResponse = fetchRoute(tour.getFrom(), tour.getTo(), tour.getTransportType());

            double timeInMinutes = mapQuestResponse.estimatedTime() / 60.0;
            tour.setTourDistance(mapQuestResponse.distance());
            tour.setEstimatedTime((int) Math.round(timeInMinutes));

            String imagePath = fetchRouteImage(mapQuestResponse);
            tour.setRouteInformation(imagePath);
        } catch (URISyntaxException | IOException e) {
            onFailure.run();
            return null;
        }

        return null;
    }

    public MapQuestResponse fetchRoute(String from, String to, String routeType) throws URISyntaxException, IOException {
        URI uri = new URIBuilder("https://www.mapquestapi.com/directions/v2/route")
                .addParameter("key", apiKey)
                .addParameter("from", from)
                .addParameter("to", to)
                .addParameter("routeType", routeType)
                .build();

        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            cancel();
        }

        try (InputStream responseStream = connection.getInputStream()) {
            JsonNode rootNode = objectMapper.readTree(responseStream);
            double distance = rootNode.at("/route/distance").asDouble();
            int timeInSeconds = rootNode.at("/route/time").asInt();

            double boundingBoxUL_Latitude, boundingBoxUL_Longitude, boundingBoxLR_Latitude, boundingBoxLR_Longitude;
            String sessionId;


            sessionId = rootNode.at("/route/sessionId").asText();
            JsonNode boundingBoxUL = rootNode.at("/route/boundingBox/ul");
            boundingBoxUL_Latitude = boundingBoxUL.at("/lat").asDouble();
            boundingBoxUL_Longitude = boundingBoxUL.at("/lng").asDouble();
            JsonNode boundingBoxLR = rootNode.at("/route/boundingBox/lr");
            boundingBoxLR_Latitude = boundingBoxLR.at("/lat").asDouble();
            boundingBoxLR_Longitude = boundingBoxLR.at("/lng").asDouble();

            return new MapQuestResponse(distance, timeInSeconds, sessionId, boundingBoxUL_Latitude, boundingBoxUL_Longitude, boundingBoxLR_Latitude, boundingBoxLR_Longitude);
        }
    }

    public String fetchRouteImage(MapQuestResponse mapQuestResponse) throws URISyntaxException, IOException {
        if (mapQuestResponse.sessionId() != null) {
            String staticMapUrl = "https://www.mapquestapi.com/staticmap/v5/map";
            URI staticMapUri = new URIBuilder(staticMapUrl)
                    .addParameter("key", apiKey)
                    .addParameter("session", mapQuestResponse.sessionId())
                    .addParameter("boundingBox", mapQuestResponse.boundingBoxUL_Latitude() + "," + mapQuestResponse.boundingBoxUL_Longitude() + "," + mapQuestResponse.boundingBoxLR_Latitude() + "," + mapQuestResponse.boundingBoxLR_Longitude())
                    .addParameter("size", "400,400")
                    .addParameter("type", "map")
                    .build();
            HttpURLConnection staticMapConnection = (HttpURLConnection) staticMapUri.toURL().openConnection();
            staticMapConnection.setRequestMethod("GET");

            if (staticMapConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                cancel();
            }

            try (InputStream staticMapStream = staticMapConnection.getInputStream()) {
                return createImageFile(staticMapStream);
            }
        }
        return null;
    }

    private static String createImageFile(InputStream staticMapStream) throws IOException {
        File directory = new File("./images/" + ImageNameGenerator.generateImageName() + ".jpg");

        if (directory.mkdirs()) {
            Files.copy(staticMapStream, Paths.get(directory.getPath()), StandardCopyOption.REPLACE_EXISTING);
        }

        return directory.getPath();
    }

    @Override
    public void setOnSuccess(Runnable runnable) {
        this.setOnSucceeded(workerStateEvent -> {
            runnable.run();
        });
    }

    @Override
    public void setOnFailure(Runnable runnable) {
        this.onFailure = runnable;
        this.setOnFailed(workerStateEvent -> {
            runnable.run();
        });
    }
}
