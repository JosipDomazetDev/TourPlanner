package com.example.tourplanner.viewmodel;

import com.example.tourplanner.data.exception.IllegalTransportTypeException;
import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.data.repository.data.MassDataRepository;
import com.example.tourplanner.data.repository.fs.FileRepository;
import com.example.tourplanner.utils.NullSafeRunner;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class MenuViewModel {
    private static final Logger logger = LogManager.getLogger(MenuViewModel.class.getSimpleName());
    private final MassDataRepository<Tour> tourRepository;
    private final FileRepository<Tour> fileRepository;
    @Setter
    private Runnable onImported;

    public MenuViewModel(MassDataRepository<Tour> tourRepository, FileRepository<Tour> fileRepository) {
        this.tourRepository = tourRepository;
        this.fileRepository = fileRepository;
    }

    public void performExport(String filePath, Consumer<List<Tour>> onSuccess, Runnable onError) {
        ArrayList<Tour> tours = tourRepository.load();
        try {
            fileRepository.exportToFile(tours, filePath);
            onSuccess.accept(tours);

            logger.info("Exported {} tours to {}", tours.size(), filePath);
        } catch (Exception e) {
            e.printStackTrace();
            onError.run();

            logger.error("Export failed!", e);
        }
    }


    public void performImport(String filePath, Consumer<List<Tour>> onSuccess, Runnable onError) {
        List<Tour> tours;
        try {
            tours = fileRepository.importFromFile(filePath);
            tourRepository.importMultiple(tours);
            NullSafeRunner.run(onImported);
            onSuccess.accept(tours);

            logger.info("Imported {} tours from {}", tours.size(), filePath);
        } catch (Exception | IllegalTransportTypeException e) {
            e.printStackTrace();
            onError.run();

            logger.error("Import failed!", e);
        }

    }
}
