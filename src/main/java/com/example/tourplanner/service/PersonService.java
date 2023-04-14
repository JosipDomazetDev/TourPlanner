package com.example.tourplanner.service;

import com.example.tourplanner.service.dto.Person;

import java.util.List;

public interface PersonService {

    List<Person> getPersonList();

    Person addNew(Person person);

    // erweitern mit parameter create new service

}
