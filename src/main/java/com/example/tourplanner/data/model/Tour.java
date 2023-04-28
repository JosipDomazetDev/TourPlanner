package com.example.tourplanner.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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
    Integer estimatedTime;
    // Image
    String routeInformation = "";


    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourLog> tourLogs = new ArrayList<>();

    public Tour(String name) {
        this.name = name;
    }

    public Tour() {
    }

    public Tour(String name, String description, String from, String to, String transportType) {
        this.name = name;
        this.tourDescription = description;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
    }


    @Override
    public String toString() {
        return name;
    }

    // write a method toSearchString that generates a searching string for me that ocntains all fields
    public String toSearchString() {
        return name + " " +
                tourDescription + " " +
                from + " " +
                to + " " +
                transportType + " " +
                tourDistance + " " +
                estimatedTime + " " +
                tourLogs.stream().map(TourLog::toSearchString).collect(Collectors.joining()).toLowerCase();
    }
}
