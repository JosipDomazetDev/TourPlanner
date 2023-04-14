package com.example.tourplanner.service.impl;

import com.example.tourplanner.persistence.entities.PersonEntity;
import com.example.tourplanner.persistence.repositories.PersonRepository;
import com.example.tourplanner.service.PersonService;
import com.example.tourplanner.service.dto.Person;
import com.example.tourplanner.service.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonMapper personMapper;

    @Override
    public List<Person> getPersonList() {
        return personMapper.fromEntity(personRepository.findAll());
    }

    @Override
    public Person addNew(Person person) {
        if (person == null){
            return null;
        }
        PersonEntity entity = personRepository.save(personMapper.toEntity(person));
        return personMapper.fromEntity(entity);
    }

}
