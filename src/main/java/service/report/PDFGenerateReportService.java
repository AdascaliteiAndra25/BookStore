package service.report;

import model.Order;

import java.util.List;

public interface PDFGenerateReportService {
    void generatePdfReport(List<Order> orders);
}
