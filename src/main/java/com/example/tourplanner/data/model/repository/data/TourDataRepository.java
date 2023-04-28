package com.example.tourplanner.data.model.repository.data;

import com.example.tourplanner.data.model.Tour;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;

public class TourDataRepository implements DataRepository<Tour> {

    EntityManager entityManager = EntityManagerProvider.getInstance();
    private static TourDataRepository instance;

    public static synchronized TourDataRepository getInstance() {
        if (instance == null) {
            instance = new TourDataRepository();
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