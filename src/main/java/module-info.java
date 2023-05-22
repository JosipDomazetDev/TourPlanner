module com.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.apache.httpcomponents.httpclient;
    requires com.fasterxml.jackson.databind;
    requires org.apache.logging.log4j;
    requires kernel;
    requires layout;
    requires io;

    exports com.example.tourplanner;
    exports com.example.tourplanner.data.model;
    exports com.example.tourplanner.ui;
    exports com.example.tourplanner.viewmodel;
    exports com.example.tourplanner.ui.components;
    exports com.example.tourplanner.ui.components.converter;
    exports com.example.tourplanner.data.repository.api;
    exports com.example.tourplanner.data.repository.data;
    exports com.example.tourplanner.data.repository.api.fetcher;
    exports com.example.tourplanner.data.exception;

    opens com.example.tourplanner.ui to javafx.fxml;
    opens com.example.tourplanner.data.model;
    opens com.example.tourplanner to javafx.fxml, org.hibernate.orm.core;
    opens com.example.tourplanner.ui.components to javafx.fxml;
    opens com.example.tourplanner.ui.components.converter to javafx.fxml;
    opens com.example.tourplanner.data.exception;
    exports com.example.tourplanner.di;
    opens com.example.tourplanner.di to javafx.fxml;
    exports com.example.tourplanner.data.repository.fs;
}