package com.example.tourplanner.data.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerProvider {
    private static  EntityManager entityManager;

    public static synchronized EntityManager getInstance() {
        if (entityManager == null) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tour");
            entityManager = entityManagerFactory.createEntityManager();
        }

        return entityManager;
    }
}
