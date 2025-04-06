package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class NLPInputParserTest {

    NLPInputParser parser = new NLPInputParser();

    @Test
    void testParseTripDetails01() {
        String input = "I'm planning a trip to Cork in January.";
        TripDetails details = parser.parseTripDetails(input);
        List<String> locations = details.getLocations();
        String monthInfo = details.getTravelMonth();

        assertNotNull(locations, "Locations not null");
        assertNotNull(monthInfo, "Month not null");
    }

    @Test
    void testParseTripDetails02() {
        String input = "I'll visit London and Berlin soon.";
        TripDetails details = parser.parseTripDetails(input);

        assertNull(details.getTravelMonth(), "Month not detected");
    }

    @Test
    void testParseTripDetails03() {
        String input = "I'll be visiting Sao Paulo, and Rio de Janeiro in December.";
        TripDetails details = parser.parseTripDetails(input);
        List<String> locations = details.getLocations();

        assertTrue(locations.contains("Sao Paulo"), "Sao Paulo as location");
        assertTrue(locations.contains("Rio de Janeiro"), "Rio de Janeiro as location");
    }
}
