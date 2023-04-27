package com.example.tourplanner.utils;

import java.util.UUID;

public class ImageNameGenerator {
    public static String generateImageName() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
