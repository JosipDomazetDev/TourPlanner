package com.example.tourplanner.data.repository.report;

import com.example.tourplanner.data.model.Tour;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class PDFReportRepository implements ReportRepository {

    @Override
    public void printTourReport(Tour selectedItem) throws IOException {
        if (selectedItem == null) return;

        double selectedTourDistance = selectedItem.getTourDistance();
        String selectedTourDescription = selectedItem.getTourDescription();
        Integer selectedTourEstimatedTime = selectedItem.getEstimatedTime();
        Integer selectedTourChildFriendliness = selectedItem.getChildFriendliness();
        String selectedTourFrom = selectedItem.getFrom();
        String selectedTourTo = selectedItem.getTo();
        String selectedTourName = selectedItem.getName();
        String selectedTourTransportType = selectedItem.getTransportType();
        String selectedTourRouteInformation = selectedItem.getRouteInformation();
        int selectedTourPopularity = selectedItem.getPopularity();
        int speed;

        String filename = "TourReport.pdf";
        PdfWriter writer = new PdfWriter(new FileOutputStream(filename));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        Paragraph title = new Paragraph("Tour Report\n\n")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setBold();
        document.add(title);

        List list = new List()
                .setSymbolIndent(20)
                .setListSymbol("•");
        list.add("Name: " + selectedTourName)
                .add("Description: " + selectedTourDescription)
                .add("From: " + selectedTourFrom)
                .add("To: " + selectedTourTo)
                .add("TransportType: " + selectedTourTransportType)
                .add("Distance: " + selectedTourDistance + "km");
        if(selectedTourEstimatedTime >= 60){
            int min = selectedTourEstimatedTime%60;
            int hour = selectedTourEstimatedTime/60;
            list.add("Estimated time : " + hour + "h " + min + "min");
        }else{
            list.add("Estimated time : " + selectedTourEstimatedTime + "min");
        }
        speed = (int) (selectedTourDistance/selectedTourEstimatedTime*60);
        list.add("Speed: " + speed + "km/h")
            .add("Child Friendliness: " + selectedTourChildFriendliness + "%")
            .add("Popularity: " + selectedTourPopularity + "%\n\n");

        document.add(list);
        Image map = new Image(ImageDataFactory.create(selectedTourRouteInformation));
        map.scaleToFit(500,500);
        document.add(map);
        document.close();

        File file = new File(filename);
        if(!Desktop.isDesktopSupported())
        {
            System.out.println("Desktop is not supported");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if(file.exists())
            desktop.open(file);

        System.out.println("PDF generated successfully");

    }

    @Override
    public void printSummaryReport(java.util.List<Tour> tours) throws IOException {
        if(tours == null){
            return;
        }
        String filename = "SummaryTourReport.pdf";
        PdfWriter writer = new PdfWriter(new FileOutputStream(filename));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        Paragraph title = new Paragraph("Summary Report\n\n")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setBold();
        document.add(title);

        List list = new List()
                .setSymbolIndent(20)
                .setListSymbol("•");

        int totalTime = 0;
        float totalDistance = 0;
        int totalChildFriendliness = 0;
        int amountOfTours = tours.size();
        int totalPopularity = 0;

        for(Tour tour : tours){
            totalTime += tour.getEstimatedTime();
            totalDistance += tour.getTourDistance();
            totalChildFriendliness += tour.getChildFriendliness();
            totalPopularity += tour.getPopularity();
        }
        int speed = (int) (totalDistance/totalTime*60);

        list.add("Amount of tours: " + amountOfTours)
                .add("Total travel distance: " + totalDistance + "km")
                .add("Average travel distance: " + (int) totalDistance/ amountOfTours + "km");
        if (totalTime < 60) {
            list.add("Total travel time: " + totalTime + "min")
                    .add("Average travel time: " + totalTime/ amountOfTours + "min");
        } else{
            list.add("Total travel time: " + totalTime/60 + "h " + totalTime%60 + "min");
            if (totalTime/tours.size() < 60){
                list.add("Average travel time: " + totalTime/ amountOfTours + "min");
            }else{
                list.add("Average travel time: " + totalTime/ amountOfTours/60 + "h " + totalTime/ amountOfTours%60 + "min");
            }
        }
        list.add("Average Speed: " + speed +"km/h")
            .add("Average Child friendliness: " + totalChildFriendliness/ amountOfTours + "%")
            .add("Average Popularity: " + totalPopularity/ amountOfTours + "%");

        document.add(list);


        int tourNumber = 1;

        for (Tour tour : tours) {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            title = new Paragraph("Tour " + tourNumber + " Report\n\n")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold();
            document.add(title);

            list = new List()
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
            speed = (int) (tour.getTourDistance()/tour.getEstimatedTime()*60);
            list.add("Speed: " + speed + "km/h")
                .add("Child Friendliness: " + tour.getChildFriendliness() + "%")
                .add("Popularity: " + tour.getPopularity() + "%\n\n");
            document.add(list);




            tourNumber++;

        }
        document.close();

        File file = new File(filename);
        if(!Desktop.isDesktopSupported())
        {
            System.out.println("Desktop is not supported");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if(file.exists())
            desktop.open(file);

        System.out.println("PDF generated successfully");
    }

}
