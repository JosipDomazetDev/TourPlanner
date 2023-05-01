package com.example.tourplanner.data.repository.data;

import com.example.tourplanner.data.model.Tour;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class TourDataRepository implements DataRepository<Tour> {
    private static final Logger logger = LogManager.getLogger(TourDataRepository.class.getSimpleName());
    EntityManager entityManager = EntityManagerProvider.getInstance();
    @Override
    public void save(Tour tour) {
        entityManager.getTransaction().begin();
        entityManager.persist(tour);
        entityManager.getTransaction().commit();

        logger.info("Created tour: {}", tour.toString());
    }

    @Override
    public void update(Tour tour) {
        entityManager.getTransaction().begin();
        entityManager.merge(tour);
        entityManager.getTransaction().commit();

        logger.info("Updated tour: {}", tour.toString());
    }

    @Override
    public void delete(Tour tour) {
        entityManager.getTransaction().begin();
        entityManager.remove(tour);
        entityManager.getTransaction().commit();

        logger.info("Deleted tour: {}", tour.toString());
    }

    @Override
    public ArrayList<Tour> load() {
        logger.info("Loaded tours");
        return new ArrayList<>(entityManager.createQuery("SELECT t FROM Tour t", Tour.class).getResultList());
    }
}