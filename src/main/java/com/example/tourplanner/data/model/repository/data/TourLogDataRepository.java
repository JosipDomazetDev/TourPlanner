package com.example.tourplanner.data.model.repository.data;

import com.example.tourplanner.data.model.TourLog;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;

public class TourLogDataRepository implements DataRepository<TourLog> {

    EntityManager entityManager = EntityManagerProvider.getInstance();
    private static TourLogDataRepository instance;

    public static synchronized TourLogDataRepository getInstance() {
        if (instance == null) {
            instance = new TourLogDataRepository();
        }
        return instance;
    }

    public TourLogDataRepository() {
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