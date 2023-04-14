package com.example.tourplanner.presentation.viewmodel;

import com.example.tourplanner.service.PersonService;
import com.example.tourplanner.service.dto.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonListViewModel {

    @Autowired
    PersonService personService;

    private List<Person> masterData = new ArrayList<>();
    private ObservableList<Person> personListItems = FXCollections.observableArrayList();

    public ObservableList<Person> getPersonListItems() {
        return personListItems;
    }

    public void addItem(Person person) {
        personListItems.add(person);
        masterData.add(person);
    }

    public void clearItems(){ personListItems.clear(); }

    public void initList(){
        personService.getPersonList().forEach(p -> {
            addItem(p);
        });
    }

    public void filterList(String searchText){
        Task<List<Person>> task = new Task<>() {
            @Override
            protected List<Person> call() throws Exception {
                updateMessage("Loading data");
                return masterData
                        .stream()
                        .filter(value -> value.getName().toLowerCase().contains(searchText.toLowerCase()))
                        .collect(Collectors.toList());
            }
        };

        task.setOnSucceeded(event -> {
            personListItems.setAll(task.getValue());
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }


}
