package com.example.tourplanner.ui;

import com.example.tourplanner.data.model.Tour;
import com.example.tourplanner.ui.components.TourCell;
import com.example.tourplanner.di.FXMLDependencyInjection;
import com.example.tourplanner.viewmodel.ToursViewModel;
import com.itextpdf.layout.element.Image;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.File;


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

        for(Tour tour: toursViewModel.getTours()){
            double TourDistance = tour.getTourDistance();
            String TourDescription = tour.getTourDescription();
            Integer TourEstimatedTime = tour.getEstimatedTime();
            Integer TourChildFriendliness = tour.getChildFriendliness();
            String TourFrom = tour.getFrom();
            String TourTo = tour.getTo();
            String TourName = tour.getName();
            String TourTransportType = tour.getTransportType();


            document.add(new Paragraph("Name: " + TourName));
            document.add(new Paragraph("Description: " + TourDescription));
            document.add(new Paragraph("From: " + TourFrom));
            document.add(new Paragraph("To: " + TourTo));
            document.add(new Paragraph("TransportType: " + TourTransportType));
            document.add(new Paragraph("Distance: " + String.valueOf(TourDistance) + "km"));
            if(TourEstimatedTime >= 60){
                int min = TourEstimatedTime%60;
                int hour = TourEstimatedTime/60;
                document.add(new Paragraph("Estimated time : " + String.valueOf(hour) + "h " + min + "min"));
            }else{
                document.add(new Paragraph("Estimated time : " + String.valueOf(TourEstimatedTime) + "min"));
            }

            document.add(new Paragraph("Child Friendliness: " + String.valueOf(TourChildFriendliness) + "%"));




        }
        document.close();
        System.out.println("PDF generated successfully");

        // KA OBS BESSER IST ES IM CONTROLLER ODER VIEWMODEL ZU MACHEN
    }

    public void printTourReport(MouseEvent mouseEvent) throws FileNotFoundException, MalformedURLException {

        var selectedItem = toursListView.getSelectionModel().getSelectedItem();
        double selectedTourDistance = selectedItem.getTourDistance();
        String selectedTourDescription = selectedItem.getTourDescription();
        Integer selectedTourEstimatedTime = selectedItem.getEstimatedTime();
        Integer selectedTourChildFriendliness = selectedItem.getChildFriendliness();
        String selectedTourFrom = selectedItem.getFrom();
        String selectedTourTo = selectedItem.getTo();
        String selectedTourName = selectedItem.getName();
        String selectedTourTransportType = selectedItem.getTransportType();
        String selectedTourRouteInformation = selectedItem.getRouteInformation();


        PdfWriter writer = new PdfWriter(new FileOutputStream("TourReport.pdf"));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Name: " + selectedTourName));
        document.add(new Paragraph("Description: " + selectedTourDescription));
        document.add(new Paragraph("From: " + selectedTourFrom));
        document.add(new Paragraph("To: " + selectedTourTo));
        document.add(new Paragraph("TransportType: " + selectedTourTransportType));
        document.add(new Paragraph("Distance: " + String.valueOf(selectedTourDistance) + "km"));
        if(selectedTourEstimatedTime >= 60){
            int min = selectedTourEstimatedTime%60;
            int hour = selectedTourEstimatedTime/60;
            document.add(new Paragraph("Estimated time : " + String.valueOf(hour) + "h " + min + "min"));
        }else{
            document.add(new Paragraph("Estimated time : " + String.valueOf(selectedTourEstimatedTime) + "min"));
        }

        document.add(new Paragraph("Child Friendliness: " + String.valueOf(selectedTourChildFriendliness) + "%"));
        Image map = new Image(ImageDataFactory.create(selectedTourRouteInformation));
        Paragraph p = new Paragraph("Map: ")
                .add(map);
        document.add(p);

        document.close();
        System.out.println("PDF generated successfully");

        //System.out.println(selectedTour);




        // KA OBS BESSER IST ES IM CONTROLLER ODER VIEWMODEL ZU MACHEN
    }
}