package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class InputParserTest {
    @Test
    void testParseTripDetails_withMonthAndLocations() {
        String input = "I'm traveling to London and Dublin in September for a vacation.";

        TripDetails details = InputParser.parseTripDetails(input);

        assertEquals("September", details.getTravelMonth(), "Month as September");
        List<String> locations = details.getLocations();
        assertTrue(locations.contains("London"), "London as a location");
        assertTrue(locations.contains("Dublin"), "Dublin as a location");
    }

    @Test
    void testParseTripDetails_withoutMonth() {
        String input = "Exploring Tokyo and Portugal during the winter!";

        TripDetails details = InputParser.parseTripDetails(input);

        assertNull(details.getTravelMonth(), "Month not detected");
        List<String> locations = details.getLocations();
        assertTrue(locations.contains("Tokyo"), "Tokyo as a location");
        assertTrue(locations.contains("Portugal"), "Portugal as a location");
    }
}
