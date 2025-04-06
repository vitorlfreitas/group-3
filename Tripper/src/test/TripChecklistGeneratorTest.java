package test.java.com.tripper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TripChecklistGeneratorTest {

    @Test
    void testGenerateChecklist_SummerMonth() {
        TripDetails details = new TripDetails("June");
        TripChecklist checklist = TripChecklistGenerator.generateChecklist(details);

        assertTrue(arrayContains(checklist.getEssentialItems(), "Light T-Shirt"));
        assertTrue(arrayContains(checklist.getRecommendations(), "Sunscreen"));
        assertTrue(arrayContains(checklist.getOptionalItems(), "Light Sweater"));
        assertTrue(arrayContains(checklist.getAccessories(), "Power Bank"));
    }

    @Test
    void testGenerateChecklist_WinterMonth() {
        TripDetails details = new TripDetails("January");
        TripChecklist checklist = TripChecklistGenerator.generateChecklist(details);

        assertTrue(arrayContains(checklist.getEssentialItems(), "Warm Jacket"));
        assertTrue(arrayContains(checklist.getRecommendations(), "Boots"));
        assertTrue(arrayContains(checklist.getOptionalItems(), "Hand Warmers"));
        assertTrue(arrayContains(checklist.getAccessories(), "Travel Adapter"));
    }

    @Test
    void testGenerateChecklist_UnknownMonth() {
        TripDetails details = new TripDetails("April");
        TripChecklist checklist = TripChecklistGenerator.generateChecklist(details);

        assertTrue(arrayContains(checklist.getEssentialItems(), "Versatile T-Shirt"));
        assertTrue(arrayContains(checklist.getRecommendations(), "Light Jacket"));
        assertTrue(arrayContains(checklist.getOptionalItems(), "Hat"));
        assertTrue(arrayContains(checklist.getAccessories(), "Portable Charger"));
    }

    @Test
    void testGenerateChecklist_NullMonth() {
        TripDetails details = new TripDetails(null);
        TripChecklist checklist = TripChecklistGenerator.generateChecklist(details);

        assertNotNull(checklist.getEssentialItems());
        assertTrue(arrayContains(checklist.getEssentialItems(), "Versatile T-Shirt"));
    }

    private boolean arrayContains(String[] array, String value) {
        for (String item : array) {
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}