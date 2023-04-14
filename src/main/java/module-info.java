module com.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires lombok;
    requires spring.context;
    requires spring.beans;
    requires spring.data.jpa;
    requires jakarta.persistence;
    requires org.slf4j;
    requires spring.boot;
    requires org.controlsfx.controls;
    requires spring.tx;
    requires spring.boot.autoconfigure;

    requires spring.core;
    requires org.hibernate.orm.core;

    opens com.example.tourplanner to spring.core;

    exports com.example.tourplanner;
    exports com.example.tourplanner.presentation;
    exports com.example.tourplanner.service;

    exports com.example.tourplanner.data.model;


    exports com.example.tourplanner.ui;
    opens com.example.tourplanner.ui to javafx.fxml;

    exports com.example.tourplanner.viewmodel;
}