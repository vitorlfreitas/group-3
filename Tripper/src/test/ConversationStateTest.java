package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

    class ConversationStateTest {

        ConversationState state = new ConversationState();

        @Test
        void testUserNameSetterGetter() {
            state.setUserName("Thales");
            assertEquals("Thales", state.getUserName());
        }

        @Test
        void testTripDetailsSetterGetter() {
            state.setTripDetails("Visiting Paris in spring");
            assertEquals("Visiting Paris in spring", state.getTripDetails());
        }

        @Test
        void testDetailsConfirmed() {
            assertFalse(state.isDetailsConfirmed()); // default is false
            state.setDetailsConfirmed(true);
            assertTrue(state.isDetailsConfirmed());
        }

        @Test
        void testAddAndGetLocations() {
            state.addLocation("Tokyo");
            state.addLocation("Kyoto");

            List<String> locations = state.getLocations();
            assertEquals(2, locations.size());
            assertTrue(locations.contains("Tokyo"));
            assertTrue(locations.contains("Kyoto"));
        }
}
