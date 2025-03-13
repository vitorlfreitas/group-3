package main.java.com.tripper;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;

public class PDFGenerator {

    public static void generateChecklist(String fileName, String checklistContent) {
        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage();
            document.addPage(page);

            // Load standard fonts using loadStandardFont
            PDType1Font fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(fontBold, 18);
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Your Trip Checklist");
            contentStream.newLineAtOffset(0, -30);
            contentStream.setFont(fontRegular, 12);
            for (String line : checklistContent.split("\n")) {
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -15);
            }
            contentStream.endText();
            contentStream.close();

            document.save(new File(fileName));
            System.out.println("PDF generated successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { document.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
