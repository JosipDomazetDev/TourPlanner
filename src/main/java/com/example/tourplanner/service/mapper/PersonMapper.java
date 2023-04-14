package com.example.tourplanner.service.mapper;

import com.example.tourplanner.persistence.entities.PersonEntity;
import com.example.tourplanner.service.dto.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper extends AbstractMapper<PersonEntity, Person> {

    @Override
    public Person fromEntity(PersonEntity entity) {
        return Person.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isEmployed(entity.isEmployed())
                .build();
    }

    @Override
    public PersonEntity toEntity(Person person) {
        return PersonEntity.builder()
                .id(person.getId())
                .name(person.getName())
                .isEmployed(person.getIsEmployed())
                .build();
    }

}
