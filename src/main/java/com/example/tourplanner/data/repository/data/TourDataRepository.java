package com.example.tourplanner.data.repository.data;

import com.example.tourplanner.data.model.Tour;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.example.tourplanner.data.repository.data.EntityManagerProvider.executeWithTransaction;

public class TourDataRepository implements MassDataRepository<Tour> {
    private static final Logger logger = LogManager.getLogger(TourDataRepository.class);
    private final EntityManager entityManager = EntityManagerProvider.getInstance();

    @Override
    public void importMultiple(List<Tour> tours) {
        executeWithTransaction(entityManager, () -> {
            for (Tour tour : tours) {
                entityManager.persist(tour);
            }
            logger.info("Imported tours: {}", tours.toString());
        });
    }

    @Override
    public void save(Tour tour) {
        executeWithTransaction(entityManager, () -> {
            entityManager.persist(tour);
            logger.info("Created tour: {}", tour.toString());
        });
    }

    @Override
    public void update(Tour tour) {
        executeWithTransaction(entityManager, () -> {
            entityManager.merge(tour);
            logger.info("Updated tour: {}", tour.toString());
        });
    }

    @Override
    public void delete(Tour tour) {
        executeWithTransaction(entityManager, () -> {
            entityManager.remove(tour);
            logger.info("Deleted tour: {}", tour.toString());
        });
    }

    @Override
    public ArrayList<Tour> load() {
        ArrayList<Tour> tours = new ArrayList<>();
        executeWithTransaction(entityManager, () -> {
            tours.addAll(entityManager.createQuery("SELECT t FROM Tour t ORDER BY t.id", Tour.class).getResultList());
            logger.info("Loaded tours");
        });
        return tours;
    }
}