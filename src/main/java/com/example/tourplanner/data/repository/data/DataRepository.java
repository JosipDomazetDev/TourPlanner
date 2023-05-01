package com.example.tourplanner.data.repository.data;

import java.util.ArrayList;

public interface DataRepository<T> {
    void save(T element);

    void update(T element);

    void delete(T element);

    ArrayList<T> load();
}
