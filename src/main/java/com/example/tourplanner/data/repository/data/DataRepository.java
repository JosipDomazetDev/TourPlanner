package com.example.tourplanner.data.repository.data;

import com.example.tourplanner.data.model.Tour;

import java.util.ArrayList;
import java.util.List;

public interface DataRepository<T> {


    void save(T element);

    void update(T element);

    void delete(T element);

    ArrayList<T> load();
}
