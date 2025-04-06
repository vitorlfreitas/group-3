package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TripDetailsTest {

    @Test
    void testSettersAndGetters() {
        TripDetails tripDetails = new TripDetails();

        List<String> expectedLocations = Arrays.asList("Goiania", "Brasilia", "Florianopolis");
        String expectedMonth = "July";

        tripDetails.setLocations(expectedLocations);
        tripDetails.setTravelMonth(expectedMonth);

        assertEquals(expectedLocations, tripDetails.getLocations());
        assertEquals(expectedMonth, tripDetails.getTravelMonth());
    }

    @Test
    void testToString() {
        TripDetails tripDetails = new TripDetails();
        tripDetails.setLocations(Arrays.asList("Lisbon", "Madrid"));
        tripDetails.setTravelMonth("September");

        String toStringResult = tripDetails.toString();

        assertTrue(toStringResult.contains("Lisbon"));
        assertTrue(toStringResult.contains("Madrid"));
        assertTrue(toStringResult.contains("September"));
    }
}
