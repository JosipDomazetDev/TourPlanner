package com.example.tourplanner.presentation.viewmodel;

import com.example.tourplanner.service.PersonService;
import com.example.tourplanner.service.dto.Person;
import javafx.beans.property.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class NewPersonViewModel {
    private SimpleLongProperty id = new SimpleLongProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleBooleanProperty isEmployed = new SimpleBooleanProperty();

    @Autowired
    private PersonService personService;
    @Autowired
    private PersonListViewModel personListViewModel;

    private Person person;


    public NewPersonViewModel() {

    }

    public NewPersonViewModel(Person person) {
        this.person = person;
        this.id = new SimpleLongProperty(person.getId());
        this.name = new SimpleStringProperty(person.getName());
        this.isEmployed = new SimpleBooleanProperty(person.getIsEmployed());
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean getIsEmployed() {
        return isEmployed.get();
    }

    public BooleanProperty isEmployedProperty() {
        return isEmployed;
    }

    public void setIsEmployed(boolean isEmployed) {
        this.isEmployed.set(isEmployed);
    }

    public void addNewPerson() {
        Person person = Person.builder().name(getName()).isEmployed(true).build();
        person = personService.addNew(person);
        personListViewModel.addItem(person);
    }


}
