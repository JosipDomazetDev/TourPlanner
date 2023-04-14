package com.example.tourplanner.service.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractMapper<E, T> implements Mapper<E, T> {

    public List<T> fromEntity(Collection<E> entities) {
        List<T> targets = new ArrayList<>();
        entities.forEach(entity -> {
            targets.add(fromEntity(entity));
        });
        return targets;
    }

    public List<E> toEntity(Collection<T> dtos) {
        List<E> entities = new ArrayList<>();
        dtos.forEach(dto -> {
            entities.add(toEntity(dto));
        });
        return entities;
    }
}
