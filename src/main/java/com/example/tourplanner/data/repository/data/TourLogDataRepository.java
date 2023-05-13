package com.example.tourplanner.data.repository.data;

import com.example.tourplanner.data.model.TourLog;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static com.example.tourplanner.data.repository.data.EntityManagerProvider.executeWithTransaction;

public class TourLogDataRepository implements DataRepository<TourLog> {
    private final EntityManager entityManager = EntityManagerProvider.getInstance();
    private static final Logger logger = LogManager.getLogger(TourLogDataRepository.class);

    public TourLogDataRepository() {
    }


    @Override
    public void save(TourLog tourLog) {
        executeWithTransaction(entityManager, () -> {
            entityManager.persist(tourLog);
            logger.info("Created tourLog: {}", tourLog.toString());
        });
    }

    @Override
    public void update(TourLog tourLog) {
        executeWithTransaction(entityManager, () -> {
            entityManager.merge(tourLog);
            logger.info("Updated tourLog: {}", tourLog.toString());
        });
    }

    @Override
    public void delete(TourLog tourLog) {
        executeWithTransaction(entityManager, () -> {
            entityManager.remove(tourLog);
            logger.info("Deleted tourLog: {}", tourLog.toString());
        });
    }

    @Override
    public ArrayList<TourLog> load() {
        ArrayList<TourLog> tourLogs = new ArrayList<>();
        executeWithTransaction(entityManager, () -> {
            tourLogs.addAll(entityManager.createQuery("SELECT t FROM TourLog t ORDER BY t.dateTime DESC", TourLog.class).getResultList());
            logger.info("Loaded tourLogs");
        });
        return tourLogs;
    }
}