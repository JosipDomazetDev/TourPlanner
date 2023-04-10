package com.example.tourplanner.data.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tour {
    String name;
    String tourDescription;
    String from;
    String to;
    String transportType;
    // Retrieved by API
    Float tourDistance;
    String estimatedTime;
    // Image
    String routeInformation;

    public Tour(String name) {
        this.name = name;
    }

    public Tour(String name, String tourDescription, String from, String to, String transportType, Float TourDistance, String estimatedTime) {
        this.name = name;
        this.tourDescription = tourDescription;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.tourDistance = TourDistance;
        this.estimatedTime = estimatedTime;
    }


    @Override
    public String toString() {
        return name;
    }
}
