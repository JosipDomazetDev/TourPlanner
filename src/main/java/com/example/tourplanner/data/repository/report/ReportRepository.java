package com.example.tourplanner.data.repository.report;

import com.example.tourplanner.data.model.Tour;

import java.io.IOException;

public interface ReportRepository {
    void printTourReport(Tour selectedItem) throws IOException;
}
