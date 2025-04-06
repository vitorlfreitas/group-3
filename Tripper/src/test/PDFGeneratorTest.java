package main.java.com.tripper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PDFGeneratorTest {

    private static final String OUTPUT_FILE = "test_checklist.pdf";

    @BeforeEach
    void setup() {
        // Delete the file if it already exists
        File file = new File(OUTPUT_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    void cleanup() {
        // Delete the file after each test
        File file = new File(OUTPUT_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testGenerateChecklist_createsPDFFile() {
        ConversationState mockState = new ConversationState("Karen");
        String[] essentials = {"Shirt", "Pants"};
        String[] recommendations = {"Jacket"};
        String[] optional = {"Hat"};
        String[] accessories = {"Sunglasses"};

        PDFGenerator.generateChecklist(OUTPUT_FILE, mockState, "Paris and Rome", essentials, recommendations, optional, accessories);

        File file = new File(OUTPUT_FILE);
        assertTrue(file.exists(), "PDF file should be created.");
        assertTrue(file.length() > 0, "PDF file should not be empty.");
    }

    @Test
    void testGenerateChecklist_containsExpectedText() throws IOException {
        ConversationState mockState = new ConversationState("Thales");
        String[] essentials = {"T-shirt"};
        String[] recommendations = {"Scarf"};
        String[] optional = {"Flip flops"};
        String[] accessories = {"Watch"};

        PDFGenerator.generateChecklist(OUTPUT_FILE, mockState, "Berlin", essentials, recommendations, optional, accessories);

        try (PDDocument doc = PDDocument.load(new File(OUTPUT_FILE))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            assertTrue(text.contains("Trip Checklist for Thales"));
            assertTrue(text.contains("Berlin"));
            assertTrue(text.contains("T-shirt"));
            assertTrue(text.contains("Scarf"));
            assertTrue(text.contains("Flip flops"));
            assertTrue(text.contains("Watch"));
        }
    }
}