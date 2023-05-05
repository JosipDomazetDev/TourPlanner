package com.example.tourplanner.data.repository.data;

import com.example.tourplanner.data.model.Tour;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class TourDataRepository implements MassDataRepository<Tour> {
    private static final Logger logger = LogManager.getLogger(TourDataRepository.class.getSimpleName());
    private final EntityManager entityManager = EntityManagerProvider.getInstance();

    @Override
    public void importMultiple(List<Tour> tours) {
        entityManager.getTransaction().begin();
        for (Tour tour : tours) {
            entityManager.persist(tour);
        }
        entityManager.getTransaction().commit();

        logger.info("Imported tours: {}", tours.toString());
    }

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
        return new ArrayList<>(entityManager.createQuery("SELECT t FROM Tour t ORDER BY t.id", Tour.class).getResultList());
    }
}