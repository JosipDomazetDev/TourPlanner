package com.example.tourplanner;

import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class TestSetup implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object o, ExtensionContext extensionContext) {
        // Disable logging before running all tests
        Configurator.setRootLevel(org.apache.logging.log4j.Level.OFF);
    }
}
