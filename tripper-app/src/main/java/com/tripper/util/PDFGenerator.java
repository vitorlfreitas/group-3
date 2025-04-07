package com.tripper.util;

import com.tripper.model.TripChecklistSection;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.elements.VerticalSpacer;
import rst.pdfbox.layout.text.Alignment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PDFGenerator {

    public static void generateStructuredChecklist(String fileName, String userName, List<TripChecklistSection> sections) {
        try (OutputStream out = new FileOutputStream(fileName)) {
            generateTripChecklistPdf(fileName, userName, sections);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateTripChecklistPdf(String fileName, String userName, List<TripChecklistSection> sections) {
        try (OutputStream out = new FileOutputStream(fileName)) {
            Document document = new Document(50, 50, 50, 50);

            // Title
            Paragraph title = new Paragraph();
            title.addText("Travel Checklist for " + userName, 20, PDType1Font.HELVETICA_BOLD);
            title.setAlignment(Alignment.Center);
            document.add(title);
            document.add(new VerticalSpacer(20));

            for (TripChecklistSection section : sections) {
                Paragraph city = new Paragraph();
                city.addText(section.getCityName(), 16, PDType1Font.HELVETICA_BOLD);
                document.add(city);

                if (section.getWeather() != null && !section.getWeather().isEmpty()) {
                    Paragraph weather = new Paragraph();
                    weather.addText("Weather: " + section.getWeather(), 12, PDType1Font.HELVETICA_OBLIQUE);
                    document.add(weather);
                }

                document.add(new VerticalSpacer(10));

                addBulletSection(document, "Clothing:", section.getClothing());
                addBulletSection(document, "Accessories:", section.getAccessories());
                addBulletSection(document, "Optional Items:", section.getOptionalItems());

                document.add(new VerticalSpacer(20));
            }

            document.save(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addBulletSection(Document document, String title, List<String> items) throws IOException {
        if (items != null && !items.isEmpty()) {
            Paragraph label = new Paragraph();
            label.addText(title, 13, PDType1Font.HELVETICA_BOLD);
            document.add(label);

            for (String item : items) {
                Paragraph itemPara = new Paragraph();
                itemPara.addText("• " + item, 12, PDType1Font.HELVETICA);
                document.add(itemPara);
            }

            document.add(new VerticalSpacer(10));
        }
    }
}