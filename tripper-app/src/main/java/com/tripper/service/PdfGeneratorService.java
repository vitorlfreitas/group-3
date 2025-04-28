package com.tripper.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

/**
 * PdfGeneratorService is a service class that generates a PDF document from text content.
 * It uses the iText library to create the PDF and format the content with different fonts and styles.
 * 
 * @author vitorlfreitas
 * @version 1.0.1
 * 
 * @see <a href="https://itextpdf.com/en/resources/examples">iText Examples</a>
 */
@Service
public class PdfGeneratorService {

    public byte[] generatePdfFromText(String title, String content, String userName) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Font userFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE);
            Font tripperFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);
            Font messageFont = new Font(Font.FontFamily.HELVETICA, 12);

            // Title
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(20f);
            document.add(titlePara);

            // Split content
            String[] lines = content.split("\n");
            String currentSpeaker = null;

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String indentSpeaker = null;
                Font speakerFont = userFont;
                float indent = 20f;

                if (line.startsWith(userName + ":")) {
                    currentSpeaker = userName;
                    indentSpeaker = userName;
                }
                else if (line.startsWith("Tripper:")) {
                    currentSpeaker = "Tripper";
                    indentSpeaker = "Tripper";
                    speakerFont = tripperFont;
                }

                if (indentSpeaker != null) {
                    String msgContent = line.substring(indentSpeaker.length() + 1).trim();

                    Paragraph messagePara = new Paragraph();
                    messagePara.setIndentationLeft(indent);
                    messagePara.setSpacingAfter(10f);

                    messagePara.add(new Chunk(indentSpeaker + ": ", speakerFont));
                    messagePara.add(new Chunk(msgContent, messageFont));

                    document.add(messagePara);
                } else {

                    Paragraph contPara = new Paragraph(line, messageFont);
                    contPara.setIndentationLeft(20f);
                    contPara.setSpacingAfter(10f);
                    document.add(contPara);
                }
            }

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
