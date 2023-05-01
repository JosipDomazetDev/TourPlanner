package com.example.tourplanner.data.repository.data;

import com.example.tourplanner.data.model.TourLog;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TourLogDataRepository implements DataRepository<TourLog> {
    EntityManager entityManager = EntityManagerProvider.getInstance();
    private static final Logger logger = LogManager.getLogger(TourLogDataRepository.class.getSimpleName());

    public TourLogDataRepository() {
    }

    @Override
    public void save(TourLog tourLog) {
        entityManager.getTransaction().begin();
        entityManager.persist(tourLog);
        entityManager.getTransaction().commit();

        logger.info("Created tourLog: {}", tourLog.toString());
    }

    @Override
    public void update(TourLog tourLog) {
        entityManager.getTransaction().begin();
        entityManager.merge(tourLog);
        entityManager.getTransaction().commit();

        logger.info("Updated tourLog: {}", tourLog.toString());
    }

    @Override
    public void delete(TourLog tourLog) {
        entityManager.getTransaction().begin();
        entityManager.remove(tourLog);
        entityManager.getTransaction().commit();

        logger.info("Deleted tourLog: {}", tourLog.toString());
    }

    @Override
    public ArrayList<TourLog> load() {
        logger.info("Loaded tourLogs");
        return new ArrayList<>(entityManager.createQuery("SELECT t FROM TourLog t", TourLog.class).getResultList());
    }
}