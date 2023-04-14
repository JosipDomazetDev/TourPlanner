package com.example.tourplanner.presentation.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SearchViewModel {

    @Autowired
    private PersonListViewModel personListViewModel;

    private SimpleStringProperty searchString = new SimpleStringProperty();


    public String getSearchString() {
        return searchString.get();
    }

    public SimpleStringProperty searchStringProperty() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString.set(searchString);
    }

    public void search() {
        personListViewModel.filterList(getSearchString());
    }



}
