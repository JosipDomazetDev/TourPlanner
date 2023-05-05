package com.example.tourplanner.data.repository.fs;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;

import java.io.IOException;
import java.util.List;

public interface FileRepository<T> {
    void exportToFile(List<T> entries, String fileName) throws IOException;

    List<T> importFromFile(String fileName) throws IOException, IllegalTransportTypeException;
}
