package com.example.tourplanner.data.repository.report;

import com.example.tourplanner.data.model.Tour;

import java.io.IOException;
import java.util.List;

public interface ReportRepository {
    void printTourReport(Tour selectedItem) throws IOException;

    void printSummaryReport(List<Tour> tours) throws IOException;
}
