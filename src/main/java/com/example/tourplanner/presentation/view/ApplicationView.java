package com.example.tourplanner.presentation.view;

import com.example.tourplanner.presentation.StageAware;
import com.example.tourplanner.presentation.events.ApplicationShutdownEvent;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class ApplicationView implements Initializable, StageAware {

    ApplicationEventPublisher publisher;

    @FXML BorderPane layout;

    // Menu, at some point break out
    @FXML MenuItem miPreferences;
    @FXML MenuItem miQuit;
    @FXML MenuItem miAbout;
    
    // Toolbar, at some point break out
    @FXML Label tbMonitorStatus;
    Circle monitorStatusIcon = new Circle(8);

    SimpleObjectProperty<Stage> stage = new SimpleObjectProperty<>();

    public ApplicationView(ApplicationEventPublisher publisher) {
        log.debug("Initializing application controller");
        this.publisher = publisher;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        stage.addListener((obv, o, n) -> n.setTitle(rb.getString("app.title")));
        tbMonitorStatus.setGraphic(monitorStatusIcon);
    }

    @FXML
    public void onFileClose(ActionEvent event) {
        publisher.publishEvent(new ApplicationShutdownEvent(event.getSource()));
    }

    @FXML 
    public void onHelpAbout(ActionEvent event) {
        new AboutDialogView().show();
    }

    @Override
    public void setStage(Stage stage) {
        this.stage.setValue(stage);
    }
}
