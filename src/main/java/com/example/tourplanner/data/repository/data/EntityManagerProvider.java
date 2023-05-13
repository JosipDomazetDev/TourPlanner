package com.example.tourplanner.data.repository.data;

import com.example.tourplanner.configuration.ConfigurationReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class EntityManagerProvider {
    private static EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(EntityManagerProvider.class);

    public static synchronized EntityManager getInstance() {
        if (entityManager == null) {
            Properties config = new Properties();

            ConfigurationReader configurationReader = ConfigurationReader.getInstance();
            config.setProperty("javax.persistence.jdbc.driver", configurationReader.getDriver());
            config.setProperty("javax.persistence.jdbc.url", configurationReader.getUrl());
            config.setProperty("javax.persistence.jdbc.user", configurationReader.getUser());
            config.setProperty("javax.persistence.jdbc.password", configurationReader.getPassword());
            config.setProperty("hibernate.dialect", configurationReader.getDialect());
            config.setProperty("hibernate.hbm2ddl.auto", configurationReader.getAuto());
            config.setProperty("hibernate.show_sql", configurationReader.getShowSql());
            config.setProperty("hibernate.format_sql", configurationReader.getFormatSql());

            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tour", config);
            entityManager = entityManagerFactory.createEntityManager();
            logger.info("Created new EntityManager");
        }

        return entityManager;
    }
}
