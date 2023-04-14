package com.example.tourplanner.data.model.repository;

import java.util.ArrayList;

public interface Repository<T> {
    void save(T element);

    void update(T element);

    void delete(T element);

    ArrayList<T> load();
}
