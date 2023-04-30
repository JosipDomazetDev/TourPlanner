package com.example.tourplanner.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "tour_log")
@Getter
@Setter
public class TourLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    private String comment;
    private int difficulty;
    private double totalTime;
    private int rating;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tour tour;

    public TourLog(Date dateTime, String comment, int difficulty, double totalTime, int rating, Tour tour) {
        this.dateTime = dateTime;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalTime = totalTime;
        this.rating = rating;
        this.tour = tour;

        tour.getTourLogs().add(this);
    }

    public TourLog() {
    }

    @Override
    public String toString() {
        return "TourLog{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", comment='" + comment + '\'' +
                ", difficulty=" + difficulty +
                ", totalTime=" + totalTime +
                ", rating=" + rating +
                ", tour=" + tour.getId() +
                '}';
    }

    public String toSearchString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String formattedDateTime = formatter.format(dateTime);

        return formattedDateTime + " " +
                comment + " " +
                difficulty + " " +
                totalTime + " " +
                rating;
    }
}