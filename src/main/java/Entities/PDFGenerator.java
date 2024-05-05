package Entities;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {
    public static void generatePDF(List<Subscription> subscriptions, String filePath, String imagePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float startX = 50; // Starting X coordinate for the border
                float startY = 650; // Initial starting Y coordinate for the title
                float width = 500; // Width of the subscription area
                float height = 100; // Height of each subscription area

                float borderThickness = 2; // Border thickness

                // Add title
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.beginText();
                contentStream.newLineAtOffset(200, startY + 10);
                contentStream.showText("Subscriptions Details");
                contentStream.endText();

                // Draw border for the title
                contentStream.setLineWidth(borderThickness);
                contentStream.addRect(startX, startY , width, 40);
                contentStream.stroke();

                // Adjust startY for subscription areas
                startY -= 50; // Move down for the title

                for (Subscription subscription : subscriptions) {
                    // Draw border rectangle
                    contentStream.setLineWidth(borderThickness);
                    contentStream.addRect(startX, startY - height, width, height);
                    contentStream.stroke();

                    // Draw subscription content
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.setNonStrokingColor(Color.BLACK);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(startX + 20, startY - 30);
                    contentStream.showText("ID: " + subscription.getId());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Plan: " + subscription.getPlan());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Duration: " + subscription.getDuration());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Type of transport: " + subscription.getTypeT().getType_t());
                    contentStream.endText();

                    // Move to next subscription area
                    startY -= (height + borderThickness); // Adjust for border thickness
                }

                // Draw border for the last subscription
                contentStream.setLineWidth(borderThickness);
                contentStream.addRect(startX, startY, width, height);
                contentStream.stroke();

                // Add image
                PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
                contentStream.drawImage(image, 450, 690, 150, 100); // Adjust image dimensions and position as needed
            }

            document.save(filePath);
        }
    }
}
