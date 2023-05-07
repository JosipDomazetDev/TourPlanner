package com.example.tourplanner.ui.components;

import com.example.tourplanner.data.model.TourLog;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ButtonCellFactory implements Callback<TableColumn<TourLog, Void>, TableCell<TourLog, Void>> {
    private final ButtonCallback<TourLog> buttonCallback;


    public ButtonCellFactory(ButtonCallback<TourLog> buttonCallback) {
        this.buttonCallback = buttonCallback;
    }

    @Override
    public TableCell<TourLog, Void> call(final TableColumn<TourLog, Void> param) {
        return new TableCell<>() {
            private final Button btn = new Button("-");
            {
                btn.getStyleClass().add("danger");
                btn.getStyleClass().add("button");
                btn.getStyleClass().add("bold");
                btn.setOnAction(event -> {
                    TourLog data = getTableRow().getItem();
                    if (data != null) {
                        buttonCallback.onButtonClicked(data);
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(btn);
                    hbox.setAlignment(javafx.geometry.Pos.CENTER);
                    hbox.setSpacing(5);
                    setGraphic(hbox);
                }
            }
        };
    }
}
