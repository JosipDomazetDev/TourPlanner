package com.example.tourplanner.data.model.repository;

import com.example.tourplanner.data.model.Tour;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;

public class TourRepository {

    EntityManager entityManager;

    public TourRepository() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tour");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void save(Tour tour) {
        entityManager.getTransaction().begin();
        entityManager.persist(tour);
        entityManager.getTransaction().commit();
    }

}