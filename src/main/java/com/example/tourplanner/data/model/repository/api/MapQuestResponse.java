package com.example.tourplanner.data.model.repository.api;


public record MapQuestResponse(double distance, int estimatedTime, String sessionId, double boundingBoxUL_Latitude,
                               double boundingBoxUL_Longitude, double boundingBoxLR_Latitude,
                               double boundingBoxLR_Longitude) {
}
