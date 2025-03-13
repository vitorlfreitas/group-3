package main.java.com.tripper;
// PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;

public class PDFGenerator {

    /**
     * Generates a custom checklist PDF with multiple sections.
     * @param fileName The output file name.
     * @param state The conversation state holding user details.
     * @param tripDetails The trip details (e.g., "Paris, Veneza and Barcelona").
     * @param essentialItems List of essential clothing items.
     * @param recommendations List of recommendations.
     * @param optionalItems List of optional items.
     * @param accessories List of accessory items.
     */
    public static void generateChecklist(String fileName, ConversationState state,
                                         String tripDetails,
                                         String[] essentialItems,
                                         String[] recommendations,
                                         String[] optionalItems,
                                         String[] accessories) {
        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Load standard fonts
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Define margins and positions
            float margin = 50;
            PDRectangle mediaBox = page.getMediaBox();
            float yStart = mediaBox.getHeight() - margin;
            float yPosition = yStart;
            float leading = 15;
            float maxWidth = mediaBox.getWidth() - 2 * margin;

            // Draw Title: Personalized Header
            contentStream.beginText();
            contentStream.setFont(fontBold, 20);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("Trip Checklist for " + state.getUserName());
            contentStream.endText();
            yPosition -= 30;

            // Section: Trip Details
            yPosition = addSectionHeader(contentStream, "Trip:", margin, yPosition, fontBold, 14);
            yPosition = addParagraph(contentStream, tripDetails, margin + 10, yPosition, fontRegular, 12, maxWidth, leading);
            yPosition -= 20;

            // Section: Essential Items
            yPosition = addSectionHeader(contentStream, "Essential:", margin, yPosition, fontBold, 14);
            yPosition = addBulletList(contentStream, essentialItems, margin + 20, yPosition, fontRegular, 12, leading);
            yPosition -= 20;

            // Section: Recommendations
            yPosition = addSectionHeader(contentStream, "Recommendations:", margin, yPosition, fontBold, 14);
            yPosition = addBulletList(contentStream, recommendations, margin + 20, yPosition, fontRegular, 12, leading);
            yPosition -= 20;

            // Section: Optional Items
            yPosition = addSectionHeader(contentStream, "Optional:", margin, yPosition, fontBold, 14);
            yPosition = addBulletList(contentStream, optionalItems, margin + 20, yPosition, fontRegular, 12, leading);
            yPosition -= 20;

            // Section: Accessories
            yPosition = addSectionHeader(contentStream, "Accessories:", margin, yPosition, fontBold, 14);
            yPosition = addBulletList(contentStream, accessories, margin + 20, yPosition, fontRegular, 12, leading);

            contentStream.close();
            document.save(new File(fileName));
            System.out.println("Custom PDF checklist generated successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { document.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    // Adds a section header and returns the new y position.
    private static float addSectionHeader(PDPageContentStream contentStream, String header,
                                          float margin, float yPosition, PDType1Font font, int fontSize) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(header);
        contentStream.endText();
        return yPosition - 20;
    }

    // Adds a bullet list; returns the new y position after adding the list.
    private static float addBulletList(PDPageContentStream contentStream, String[] items,
                                       float xPosition, float yPosition, PDType1Font font, int fontSize, float leading) throws IOException {
        for (String item : items) {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(xPosition, yPosition);
            contentStream.showText("• " + item);
            contentStream.endText();
            yPosition -= leading;
        }
        return yPosition;
    }

    // Utility method to add text with automatic wrapping. Returns the new y position.
    private static float addParagraph(PDPageContentStream contentStream, String text, float xPosition, float yPosition,
                                      PDType1Font font, int fontSize, float maxWidth, float leading) throws IOException {
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(xPosition, yPosition);
        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            float textWidth = font.getStringWidth(testLine) / 1000 * fontSize;
            if (textWidth > maxWidth) {
                contentStream.showText(line.toString());
                contentStream.endText();
                yPosition -= leading;
                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                contentStream.newLineAtOffset(xPosition, yPosition);
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) {
                    line.append(" ");
                }
                line.append(word);
            }
        }
        contentStream.showText(line.toString());
        contentStream.endText();
        return yPosition - leading;
    }
}
