package service.report;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import model.Order;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PDFGenerateReport implements PDFGenerateReportService {

    public void generatePdfReport(List<Order> orders) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filePath = "C:\\Users\\Windows 10 PRO\\Desktop\\IS\\LibraryProject\\AllOrders_Report_" + timestamp + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Paragraph title = new Paragraph("Report for All Orders in the Last Month")
                    .setBold()
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setFontSize(16);
            document.add(title);

            document.add(new Paragraph("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT));
            document.add(new Paragraph("\n"));


            float[] columnWidths = {50, 150, 150, 50, 50, 100, 50};
            Table table = new Table(columnWidths);


            table.addHeaderCell(new Cell().add(new Paragraph("Order ID").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Book Title").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Book Author").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Price").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Timestamp").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Employee ID").setBold()));


            for (Order order : orders) {
                table.addCell(String.valueOf(order.getId()));
                table.addCell(order.getTitle());
                table.addCell(order.getAuthor());
                table.addCell(String.format("%.2f", order.getPrice()));
                table.addCell(String.valueOf(order.getQuantity()));
                table.addCell(order.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                table.addCell(String.valueOf(order.getUserId()));
            }


            document.add(table);

            document.close();
            System.out.println("PDF Report for all orders generated at: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
