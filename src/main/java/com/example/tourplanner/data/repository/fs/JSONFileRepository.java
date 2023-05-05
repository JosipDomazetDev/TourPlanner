package com.example.tourplanner.data.repository.fs;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JSONFileRepository implements FileRepository<Tour> {
    @Override
    public void exportToFile(List<Tour> tours, String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ArrayNode toursNode = rootNode.putArray("tours");

        for (Tour tour : tours) {
            ObjectNode tourNode = toursNode.addObject();
            tourNode.put("name", tour.getName());
            tourNode.put("tourDescription", tour.getTourDescription());
            tourNode.put("tourDistance", tour.getTourDistance());
            tourNode.put("from", tour.getFrom());
            tourNode.put("to", tour.getTo());
            tourNode.put("transportType", tour.getTransportType());
            tourNode.put("mapType", tour.getMapType());
            tourNode.put("estimatedTime", tour.getEstimatedTime());
            tourNode.put("routeInformation", tour.getRouteInformation());
            tourNode.put("popularity", tour.getPopularity());
            tourNode.put("childFriendliness", tour.getChildFriendliness());

            ArrayNode logsNode = tourNode.putArray("logs");
            for (TourLog log : tour.getTourLogs()) {
                ObjectNode logNode = logsNode.addObject();
                logNode.put("dateTime", log.getDateTime().toInstant().toString());
                logNode.put("comment", log.getComment());
                logNode.put("difficulty", log.getDifficulty());
                logNode.put("totalTime", log.getTotalTime());
                logNode.put("rating", log.getRating());
            }
        }

        objectMapper.writeValue(new File(fileName), rootNode);
    }


    @Override
    public List<Tour> importFromFile(String fileName) throws IOException, IllegalTransportTypeException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Tour> tours = new ArrayList<>();
        ObjectNode rootNode = (ObjectNode) objectMapper.readTree(new File(fileName));
        ArrayNode toursNode = (ArrayNode) rootNode.get("tours");

        for (JsonNode tourNode : toursNode) {
            Tour tour = new Tour(
                    tourNode.get("name").asText(),
                    tourNode.get("tourDescription").asText(),
                    tourNode.get("tourDistance").asDouble(),
                    tourNode.get("from").asText(),
                    tourNode.get("to").asText(),
                    tourNode.get("transportType").asText(),
                    tourNode.get("mapType").asText(),
                    tourNode.get("estimatedTime").asInt(),
                    tourNode.get("routeInformation").asText(),
                    tourNode.get("popularity").asInt(),
                    tourNode.get("childFriendliness").asInt()
            );

            ArrayNode logsNode = (ArrayNode) tourNode.get("logs");
            for (JsonNode logNode : logsNode) {
                new TourLog(
                        Date.from(Instant.parse(logNode.get("dateTime").asText())),
                        logNode.get("comment").asText(),
                        logNode.get("difficulty").asInt(),
                        logNode.get("totalTime").asDouble(),
                        logNode.get("rating").asInt(),
                        tour
                );
            }

            tours.add(tour);
        }
        return tours;
    }


}
