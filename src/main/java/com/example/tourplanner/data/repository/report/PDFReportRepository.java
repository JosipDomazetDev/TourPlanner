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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;

public class PDFReportRepository implements ReportRepository {

    @Override
    public void printTourReport(Tour selectedItem) throws FileNotFoundException, MalformedURLException {
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


        PdfWriter writer = new PdfWriter(new FileOutputStream("TourReport.pdf"));
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

        list.add("Child Friendliness: " + selectedTourChildFriendliness + "%\n\n");
        document.add(list);
        Image map = new Image(ImageDataFactory.create(selectedTourRouteInformation));
        map.scaleToFit(500,500);
        document.add(map);



        document.close();
        System.out.println("PDF generated successfully");

    }

    @Override
    public void printSummaryReport(java.util.List<Tour> tours) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(new FileOutputStream("SummaryTourReport.pdf"));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        int tourNumber = 1;

        for (Tour tour : tours) {
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
    }

}
