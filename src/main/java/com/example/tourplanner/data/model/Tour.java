package com.example.tourplanner.data.model;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.utils.ImageNameGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String tourDescription;
    @Column(name = "from_place")
    String from;
    @Column(name = "to_place")
    String to;
    String transportType;
    // Retrieved by API
    double tourDistance;
    // In minutes
    Integer estimatedTime;
    // Image
    String routeInformation = "";
    String imageId = "";
    String mapType = "";


    private Integer popularity;

    private Integer childFriendliness;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @OrderBy("dateTime DESC")
    private List<TourLog> tourLogs = new ArrayList<>();

    public Tour(String name) {
        this.name = name;
    }

    public Tour() {
    }

    public String getImageId() {
        if (this.imageId == null || this.imageId.isEmpty()) {
            this.imageId = ImageNameGenerator.generateImageName();
        }
        return imageId;
    }

    public Tour(String name, String description, Double tourDistance, String from, String to, String transportType, String mapType, int estimatedTime, String routeInformation, int popularity, int childFriendliness) throws IllegalTransportTypeException {
        this(name, description, from, to, transportType, mapType);
        this.tourDistance = tourDistance;
        this.estimatedTime = estimatedTime;
        this.routeInformation = routeInformation;
        this.popularity = popularity;
        this.childFriendliness = childFriendliness;
    }

    public Tour(String name, String description, String from, String to, String transportType, String mapType) throws IllegalTransportTypeException {
        if (checkIfTransportTypeIsValid(transportType)) {
            throw new IllegalTransportTypeException();
        }

        this.name = name;
        this.tourDescription = description;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.mapType = mapType;
    }

    @PrePersist
    @PreUpdate
    public void calculateDerivedFields() {
        calculatePopularity();
        calculateChildFriendliness();
    }

    private void calculatePopularity() {
        int i = 10;

        int ret = Math.round((float) tourLogs.size() / i * 100);
        this.popularity = Math.min(100, ret);
    }

    private void calculateChildFriendliness() {
        double totalDifficulty = 0;
        double totalTime = 0;

        for (TourLog log : tourLogs) {
            totalDifficulty += log.getDifficulty();
            totalTime += log.getTotalTime();
        }

        double difficultyAvg = totalDifficulty / tourLogs.size();
        // 1 is subtracted from the average difficulty because that is still considered an easy tour
        difficultyAvg = Math.max(difficultyAvg - 1, 0);

        double timeAvg = totalTime / tourLogs.size();
        // 30 Minutes are subtracted from the average time because that is still considered a short tour
        timeAvg = Math.max(timeAvg - 30, 1);

        double tourDistance = this.tourDistance;
        // 5 km are subtracted from the tour distance because that is still considered a short tour
        tourDistance -= 5;

        // Calculate the child friendliness value based on the difficulty, time, and distance
        int childFriendliness = (int) Math.round(100 - difficultyAvg * 10 - (timeAvg / 60) * 20 - tourDistance * 2);

        // Make sure the child friendliness value is within the range of 1 to 100
        childFriendliness = Math.max(childFriendliness, 1);
        childFriendliness = Math.min(childFriendliness, 100);

        this.childFriendliness = childFriendliness;
    }


    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tourDescription='" + tourDescription + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", transportType='" + transportType + '\'' +
                ", transportType='" + mapType + '\'' +
                ", tourDistance=" + tourDistance +
                ", estimatedTime=" + estimatedTime +
                ", routeInformation='" + routeInformation + '\'' +
                ", tourLogs=" + tourLogs +
                '}';
    }

    // write a method toSearchString that generates a searching string for me that contains all fields
    public String toSearchString() {
        String ret = name + " " +
                tourDescription + " " +
                from + " " +
                to + " " +
                transportType + " " +
                mapType + " " +
                tourDistance + " " +
                estimatedTime + " " +
                childFriendliness + "% " +
                popularity + "% " +
                tourLogs.stream().map(TourLog::toSearchString).collect(Collectors.joining()).toLowerCase();

        return ret.toLowerCase();
    }


    public static final String[] validTransportTypes = {"fastest", "shortest", "walk", "bicycle"};

    private static boolean checkIfTransportTypeIsValid(String transportType) {
        return !Arrays.asList(validTransportTypes).contains(transportType.toLowerCase());
    }

    public void setTransportType(String transportType) throws IllegalTransportTypeException {
        if (checkIfTransportTypeIsValid(transportType)) {
            throw new IllegalTransportTypeException();
        }

        this.transportType = transportType;
    }

    public String getImageFilename(String fileType) {
        return getImageFilename(mapType, fileType);
    }

    public String getImageFilename(String mapType, String fileType) {
        return getImageId() + mapType + "." + fileType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tour tour)) return false;
        return Double.compare(tour.tourDistance, tourDistance) == 0 && Objects.equals(id, tour.id) && Objects.equals(name, tour.name) && Objects.equals(tourDescription, tour.tourDescription) && Objects.equals(from, tour.from) && Objects.equals(to, tour.to) && Objects.equals(transportType, tour.transportType) && Objects.equals(estimatedTime, tour.estimatedTime) && Objects.equals(routeInformation, tour.routeInformation) && Objects.equals(imageId, tour.imageId) && Objects.equals(mapType, tour.mapType) && Objects.equals(popularity, tour.popularity) && Objects.equals(childFriendliness, tour.childFriendliness) && Objects.equals(tourLogs, tour.tourLogs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tourDescription, from, to, transportType, tourDistance, estimatedTime, routeInformation, imageId, mapType, popularity, childFriendliness, tourLogs);
    }
}
