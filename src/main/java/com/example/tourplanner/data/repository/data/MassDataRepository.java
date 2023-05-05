package com.example.tourplanner.data.repository.data;

import java.util.List;

public interface MassDataRepository<T> extends DataRepository<T>{
    void importMultiple(List<T> tours);
}
