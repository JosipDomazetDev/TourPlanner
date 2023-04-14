package com.example.tourplanner.presentation.view;

import com.example.tourplanner.presentation.viewmodel.NewPersonViewModel;
import com.example.tourplanner.service.PersonService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class NewPersonView implements Initializable {

    @Autowired
    private PersonService personService;
    @Autowired
    private SearchView searchView;
    @Autowired
    private NewPersonViewModel newPersonViewModel;

    @FXML
    private Text feedbackText;
    @FXML
    private TextField nameTextField;

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        nameTextField.textProperty().bindBidirectional(newPersonViewModel.nameProperty());
    }

    public void submitButtonAction(ActionEvent event) {
        if (nameTextField.getText().isEmpty()) {
            feedbackText.setText("nothing entered!");
            return;
        }

        newPersonViewModel.addNewPerson();
    }
}
