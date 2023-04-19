package com.example.tourplanner.data.model.repository;

import com.example.tourplanner.data.model.Tour;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;

public class TourRepository implements Repository<Tour> {

    EntityManager entityManager;
    private static TourRepository instance;

    public static synchronized TourRepository getInstance() {
        if (instance == null) {
            instance = new TourRepository();
        }
        return instance;
    }

    public TourRepository() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tour");
        entityManager = entityManagerFactory.createEntityManager();
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