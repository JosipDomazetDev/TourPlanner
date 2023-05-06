package com.example.tourplanner.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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

    @PrePersist
    @PreUpdate
    @PreRemove
    private void calculateDerivedFields() {
        this.tour.calculateDerivedFields();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourLog tourLog)) return false;
        return difficulty == tourLog.difficulty && Double.compare(tourLog.totalTime, totalTime) == 0 && rating == tourLog.rating && Objects.equals(id, tourLog.id) && Objects.equals(dateTime, tourLog.dateTime) && Objects.equals(comment, tourLog.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, comment, difficulty, totalTime, rating, tour);
    }
}