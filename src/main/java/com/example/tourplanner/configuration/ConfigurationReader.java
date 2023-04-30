package com.example.tourplanner.configuration;

import com.example.tourplanner.data.model.repository.api.fetcher.MapQuestAPIFetcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
    private static final Logger logger = LogManager.getLogger(ConfigurationReader.class.getSimpleName());
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static final String API_KEY_PROPERTY = "api_key";

    private static ConfigurationReader instance;
    private String apiKey;

    private ConfigurationReader() {
        Properties props = new Properties();
        logger.info("Reading configuration from {}", CONFIG_FILE_PATH);
        try (FileInputStream in = new FileInputStream(CONFIG_FILE_PATH)) {
            props.load(in);
            apiKey = props.getProperty(API_KEY_PROPERTY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConfigurationReader getInstance() {
        if (instance == null) {
            instance = new ConfigurationReader();
        }
        return instance;
    }

    public synchronized String getApiKey() {
        return apiKey;
    }

}
