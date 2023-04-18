package com.example.tourplanner.data.model.repository;

import com.example.tourplanner.data.model.TourLog;
import com.example.tourplanner.data.model.TourLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;

public class TourLogRepository implements Repository<TourLog> {

    EntityManager entityManager;

    public TourLogRepository() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tour");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void save(TourLog tourLog) {
        entityManager.getTransaction().begin();
        entityManager.persist(tourLog);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(TourLog tourLog) {
        entityManager.getTransaction().begin();
        entityManager.merge(tourLog);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(TourLog tourLog) {
        entityManager.getTransaction().begin();
        entityManager.remove(tourLog);
        entityManager.getTransaction().commit();
    }

    @Override
    public ArrayList<TourLog> load() {
        return new ArrayList<>(entityManager.createQuery("SELECT t FROM TourLog t", TourLog.class).getResultList());
    }
}