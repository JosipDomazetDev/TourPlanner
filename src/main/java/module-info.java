module com.example.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires lombok;

    opens com.example.tourplanner to javafx.fxml;
    exports com.example.tourplanner;

    exports com.example.tourplanner.ui;
    opens com.example.tourplanner.ui to javafx.fxml;

    exports com.example.tourplanner.viewmodel;
}