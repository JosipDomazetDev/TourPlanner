module com.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    exports com.example.tourplanner;
    exports com.example.tourplanner.data.model;
    exports com.example.tourplanner.ui;
    exports com.example.tourplanner.viewmodel;

    opens com.example.tourplanner.ui to javafx.fxml;
    opens com.example.tourplanner.data.model;
    opens com.example.tourplanner to javafx.fxml, org.hibernate.orm.core ;
    exports com.example.tourplanner.ui.components;
    opens com.example.tourplanner.ui.components to javafx.fxml;
    exports com.example.tourplanner.ui.components.converter;
    opens com.example.tourplanner.ui.components.converter to javafx.fxml;
}