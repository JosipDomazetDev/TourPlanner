package com.example.tourplanner.data.model.repository;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.model.TourLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;

public class TourRepository implements Repository<Tour> {

    EntityManager entityManager = EntityManagerProvider.getInstance();
    private static TourRepository instance;

    public static synchronized TourRepository getInstance() {
        if (instance == null) {
            instance = new TourRepository();
        }
        return instance;
    }

    @Override
    public void save(Tour tour) {
        entityManager.getTransaction().begin();
        entityManager.persist(tour);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Tour tour) {
        entityManager.getTransaction().begin();
        entityManager.merge(tour);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Tour tour) {
        entityManager.getTransaction().begin();
        entityManager.remove(tour);
        entityManager.getTransaction().commit();
    }

    @Override
    public ArrayList<Tour> load() {
        return new ArrayList<>(entityManager.createQuery("SELECT t FROM Tour t", Tour.class).getResultList());
    }
}