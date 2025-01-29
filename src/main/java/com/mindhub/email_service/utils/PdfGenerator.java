package com.mindhub.email_service.utils;

import com.mindhub.email_service.dtos.NewOrderEntityDTO;
import com.mindhub.email_service.dtos.NewOrderItemEntityDTO;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.File;

public class PdfGenerator {

    public String createPdf(NewOrderEntityDTO orderDetails) throws IOException {
        if (orderDetails == null) {
            throw new IllegalArgumentException("Order details cannot be null.");
        }

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><body>");
        htmlContent.append("<h1>Order Confirmation</h1>");
        htmlContent.append("<p>User: ").append(orderDetails.getUserEmail()).append("</p>");
        htmlContent.append("<h3>Products:</h3>");
        htmlContent.append("<ul>");

        for (NewOrderItemEntityDTO item : orderDetails.getProducts()) {
            htmlContent.append("<li>Product ID: ").append(item.getProductId())
                    .append(", Quantity: ").append(item.getQuantity()).append("</li>");
        }

        htmlContent.append("</ul>");
        htmlContent.append("</body></html>");

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
