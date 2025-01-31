package com.mindhub.email_service.utils;

import com.mindhub.email_service.dtos.NewOrderEntityDTO;
import com.mindhub.email_service.dtos.NewOrderItemEntityDTO;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.File;
import java.util.stream.Collectors;

public class PdfGenerator {

    public String createPdf(NewOrderEntityDTO orderDetails) throws IOException {
        if (orderDetails == null) {
            throw new IllegalArgumentException("Order details cannot be null.");
        }

        String htmlContent = String.format(
                "<html><body>" +
                        "<h1>Order Confirmation</h1>" +
                        "<p>User: %s</p>" +
                        "<h3>Products:</h3>" +
                        "<table border='1' cellspacing='0' cellpadding='5'>" +
                        "<tr>" +
                        "<th>Product ID</th>" +
                        "<th>Quantity</th>" +
                        "</tr>" +
                        "%s" +
                        "</table>" +
                        "</body></html>",
                orderDetails.getUserEmail(),
                orderDetails.getProducts().stream()
                        .map(item -> String.format(
                                "<tr><td>%d</td><td>%d</td></tr>",
                                item.getProductId(), item.getQuantity()))
                        .collect(Collectors.joining())
        );

        String pdfPath = "/tmp/order-" + orderDetails.getUserEmail() + ".pdf";
        File pdfFile = new File(pdfPath);

        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent.toString());
            renderer.layout();

            try (FileOutputStream os = new FileOutputStream(pdfFile)) {
                renderer.createPDF(os);
            }

            if (pdfFile.exists() && pdfFile.length() > 0) {
                return pdfPath;
            } else {
                throw new IOException("PDF file was not created successfully.");
            }
        } catch (Exception e) {
            throw new IOException("Error generating PDF", e);
        }
    }
}
