package com.example.tourplanner.persistence.repositories;


import com.example.tourplanner.persistence.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {


}
