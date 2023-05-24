package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.di.FXMLDependencyInjection;
import com.example.tourplanner.ui.components.TourCell;
import com.example.tourplanner.viewmodel.ToursViewModel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


public class ToursController implements Initializable {
    @FXML
    public ListView<Tour> toursListView;

    @FXML
    public TextField searchTextField;

    private final ToursViewModel toursViewModel;


    public ToursController(ToursViewModel toursViewModel) {
        this.toursViewModel = toursViewModel;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toursListView.itemsProperty().bindBidirectional(toursViewModel.getProperty());
        toursListView.setCellFactory(listView -> new TourCell());

        if (toursListView.getItems().isEmpty()) {
            // Empty
        } else {
            toursListView.getSelectionModel().select(0);
            toursViewModel.setSelectedTour(toursListView.getSelectionModel().getSelectedItem());
        }

        toursViewModel.getTours().addListener((ListChangeListener<Tour>) c -> {
            // Refresh the ListView when the list changes
            // This is needed for the tour update to be reflected immediately
            toursListView.refresh();
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            toursViewModel.performSearch(newValue);
        });
    }

    @FXML
    private void onTourClick(MouseEvent event) {
        Tour selectedTour = toursListView.getSelectionModel().getSelectedItem();
        toursViewModel.setSelectedTour(selectedTour);
    }

    public void onAddTour(MouseEvent mouseEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLDependencyInjection.load("create-tour.fxml", Locale.ENGLISH);
            Scene scene = new Scene(root);
            stage.setTitle("Create Tour");
            scene.getStylesheets().add(getClass().getResource("/css/bootsstrap.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/custom.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // TODO CADE HIER FÜR DICH
    public void printSummaryReport(MouseEvent mouseEvent) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(new FileOutputStream("SummaryTourReport.pdf"));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        int tourNumber = 1;

        for (Tour tour : toursViewModel.getTours()) {
            Paragraph title = new Paragraph("Tour " + tourNumber + " Report\n\n")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold();
            document.add(title);

            List list = new List()
                    .setSymbolIndent(20)
                    .setListSymbol("•");
            list.add("Name: " + tour.getName())
                    .add("Description: " + tour.getTourDescription())
                    .add("From: " + tour.getFrom())
                    .add("To: " + tour.getTo())
                    .add("TransportType: " + tour.getTransportType())
                    .add("Distance: " + tour.getTourDistance() + "km");
            int TourEstimatedTime = tour.getEstimatedTime();
            if (TourEstimatedTime >= 60) {
                int min = TourEstimatedTime % 60;
                int hour = TourEstimatedTime / 60;
                list.add("Estimated time : " + hour + "h " + min + "min");
            } else {
                list.add("Estimated time : " + TourEstimatedTime + "min");
            }

            list.add("Child Friendliness: " + tour.getChildFriendliness() + "%\n\n");
            document.add(list);

            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            tourNumber++;

        }
        document.close();
        System.out.println("PDF generated successfully");

        // KA OBS BESSER IST ES IM CONTROLLER ODER VIEWMODEL ZU MACHEN
    }

    public void printTourReport(MouseEvent mouseEvent) {
        Tour selectedItem = toursListView.getSelectionModel().getSelectedItem();

        try {
            toursViewModel.printTourReport(selectedItem);
        } catch (IOException e) {
            // Display error message
        }

        //System.out.println(selectedTour);


        // KA OBS BESSER IST ES IM CONTROLLER ODER VIEWMODEL ZU MACHEN
    }
}