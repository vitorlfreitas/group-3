package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TripChecklistTest {

    @Test
    void testTripChecklistInitializationAndGetters() {
        String[] essentials = {"T-shirt", "Jeans"};
        String[] recommendations = {"Umbrella"};
        String[] optional = {"Hat"};
        String[] accessories = {"Sunglasses", "Watch"};

        TripChecklist checklist = new TripChecklist(essentials, recommendations, optional, accessories);

        assertArrayEquals(essentials, checklist.getEssentialItems());
        assertArrayEquals(recommendations, checklist.getRecommendations());
        assertArrayEquals(optional, checklist.getOptionalItems());
        assertArrayEquals(accessories, checklist.getAccessories());
    }
}