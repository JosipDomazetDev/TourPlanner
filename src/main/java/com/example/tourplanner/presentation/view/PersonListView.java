package com.example.tourplanner.presentation.view;

import com.example.tourplanner.presentation.viewmodel.PersonListViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class PersonListView implements Initializable{

    @Autowired
    public PersonListViewModel personListViewModel;

    @FXML
    public TableView tableView = new TableView<>();
    @FXML
    private VBox dataContainer;

    @Override
    public void initialize(URL location, ResourceBundle rb){
        tableView.setItems(personListViewModel.getPersonListItems());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory("id"));
        TableColumn name = new TableColumn("NAME");
        name.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn employed = new TableColumn("EMPLOYED");
        employed.setCellValueFactory(new PropertyValueFactory("isEmployed"));
        tableView.getColumns().addAll(id, name, employed);

        dataContainer.getChildren().add(tableView);
        personListViewModel.initList();
    }

}
