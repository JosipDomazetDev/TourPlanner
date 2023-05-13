package com.example.tourplanner.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
    private static final Logger logger = LogManager.getLogger(ConfigurationReader.class.getSimpleName());
    private static final String CONFIG_FILE_PATH = "config.properties";
    private static final String API_KEY_PROPERTY = "api_key";
    private static final String DRIVER_KEY_PROPERTY = "driver";
    private static final String URL_KEY_PROPERTY = "url";
    private static final String USER_KEY_PROPERTY = "user";
    private static final String PASSWORD_KEY_PROPERTY = "password";
    private static final String DIALECT_KEY_PROPERTY = "dialect";
    private static final String AUTO_KEY_PROPERTY = "auto";
    private static final String SHOW_SQL_KEY_PROPERTY = "show_sql";
    private static final String FORMAT_SQL_KEY_PROPERTY = "format_sql";

    private static ConfigurationReader instance;
    private String apiKey;
    private String driver;
    private String url;
    private String user;
    private String password;
    private String dialect;
    private String auto;
    private String showSql;
    private String formatSql;

    private ConfigurationReader() {
        Properties props = new Properties();
        logger.info("Reading configuration from {}", CONFIG_FILE_PATH);
        try (FileInputStream in = new FileInputStream(CONFIG_FILE_PATH)) {
            props.load(in);
            apiKey = props.getProperty(API_KEY_PROPERTY);
            driver = props.getProperty(DRIVER_KEY_PROPERTY);
            url = props.getProperty(URL_KEY_PROPERTY);
            user = props.getProperty(USER_KEY_PROPERTY);
            password = props.getProperty(PASSWORD_KEY_PROPERTY);
            dialect = props.getProperty(DIALECT_KEY_PROPERTY);
            auto = props.getProperty(AUTO_KEY_PROPERTY);
            showSql = props.getProperty(SHOW_SQL_KEY_PROPERTY);
            formatSql = props.getProperty(FORMAT_SQL_KEY_PROPERTY);
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

    public synchronized String getDriver() {
        return driver;
    }

    public synchronized String getUrl() {
        return url;
    }

    public synchronized String getUser() {
        return user;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized String getDialect() {
        return dialect;
    }

    public synchronized String getAuto() {
        return auto;
    }

    public synchronized String getShowSql() {
        return showSql;
    }

    public synchronized String getFormatSql() {
        return formatSql;
    }
}
