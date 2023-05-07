package com.example.tourplanner.data.repository.api.fetcher;

import com.example.tourplanner.data.model.Tour;

import javafx.concurrent.Task;
import org.apache.http.client.utils.URIBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapQuestAPIFetcher extends Task<Void> implements MapAPIFetcher {
    private final String apiKey;
    private final Tour tour;
    private final ObjectMapper objectMapper;
    private Runnable onFailure;
    private static final Logger logger = LogManager.getLogger(MapQuestAPIFetcher.class.getSimpleName());

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
            tour.setTourDistance((int) Math.round(mapQuestResponse.distance()));
            tour.setEstimatedTime((int) Math.round(timeInMinutes));

            String imagePath = fetchRouteImage(mapQuestResponse, tour.getImageFilename("jpg"), tour.getMapType());
            tour.setRouteInformation(imagePath);
        } catch (URISyntaxException | IOException e) {
            onFailure.run();
            logger.error(e.toString());
            return null;
        }

        return null;
    }

    public static String mapVerboseRouteType(String verboseString) {
        return switch (verboseString.toLowerCase()) {
            case "fastest" -> "fastest";
            case "shortest" -> "shortest";
            case "walk" -> "pedestrian";
            case "bicycle" -> "bicycle";
            default -> null;
        };
    }


    public MapQuestResponse fetchRoute(String from, String to, String routeType) throws URISyntaxException, IOException {
        URI uri = new URIBuilder("https://www.mapquestapi.com/directions/v2/route")
                .addParameter("key", apiKey)
                .addParameter("from", from)
                .addParameter("to", to)
                .addParameter("routeType", mapVerboseRouteType(routeType))
                .build();

        HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setRequestMethod("GET");

        logger.info("{} from {}", connection.getResponseCode(), uri.toString());

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

    public static String mapVerboseMapType(String verboseString) {
        return switch (verboseString.toLowerCase()) {
            case "map" -> "map";
            case "hybrid" -> "hyb";
            case "satellite" -> "sat";
            case "light" -> "light";
            case "dark" -> "dark";
            default -> null;
        };
    }

    public String fetchRouteImage(MapQuestResponse mapQuestResponse, String imageName, String mapType) throws URISyntaxException, IOException {
        if (mapQuestResponse.sessionId() != null) {
            String staticMapUrl = "https://www.mapquestapi.com/staticmap/v5/map";
            URI staticMapUri = new URIBuilder(staticMapUrl)
                    .addParameter("key", apiKey)
                    .addParameter("session", mapQuestResponse.sessionId())
                    .addParameter("boundingBox", mapQuestResponse.boundingBoxUL_Latitude() + "," + mapQuestResponse.boundingBoxUL_Longitude() + "," + mapQuestResponse.boundingBoxLR_Latitude() + "," + mapQuestResponse.boundingBoxLR_Longitude())
                    .addParameter("size", "600,500")
                    .addParameter("format", "jpg90")
                    .addParameter("type", mapVerboseMapType(mapType))
                    .build();
            HttpURLConnection staticMapConnection = (HttpURLConnection) staticMapUri.toURL().openConnection();
            staticMapConnection.setRequestMethod("GET");

            logger.info("{} from {}", staticMapConnection.getResponseCode(), staticMapUri.toString());

            if (staticMapConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                cancel();
            }

            try (InputStream staticMapStream = staticMapConnection.getInputStream()) {
                return createImageFile(staticMapStream, imageName);
            }
        }
        return null;
    }

    private static String createImageFile(InputStream staticMapStream, String imageName) throws IOException {
        File directory = new File("./images/" + imageName);
        directory.mkdirs();
        logger.info("Created image {}", directory.getPath());

        Files.copy(staticMapStream, Paths.get(directory.getPath()), StandardCopyOption.REPLACE_EXISTING);

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
