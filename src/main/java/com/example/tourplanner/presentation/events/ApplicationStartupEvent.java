package com.example.tourplanner.presentation.events;

import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class ApplicationStartupEvent extends ApplicationEvent {

    @Getter 
    private Stage stage;

    public ApplicationStartupEvent(Object source, Stage stage) {
        super(source);
        this.stage = stage;
    }
}
