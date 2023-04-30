package com.example.tourplanner.data.model.repository.data;

import com.example.tourplanner.configuration.ConfigurationReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityManagerProvider {
    private static  EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(EntityManagerProvider.class.getSimpleName());

    public static synchronized EntityManager getInstance() {
        if (entityManager == null) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tour");
            entityManager = entityManagerFactory.createEntityManager();
            logger.info("Created new EntityManager");
        }

        return entityManager;
    }
}
