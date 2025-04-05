package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConversationManagerTest {

    ConversationManager conversationManager = new ConversationManager();

    @Test
    void testGetGreeting() {
        String userName01 = "Vitor";
        String expected01 = "Hello Vitor! I'm Tripper, your friendly travel clothing planner. "
                + "I’m here to help you pack perfectly for your adventure!";
        String userName02 = "";
        String expected02 = "Hello ! I'm Tripper, your friendly travel clothing planner. "
                + "I’m here to help you pack perfectly for your adventure!";
        String userName03 = "12#";
        String expected03 = "Hello 12#! I'm Tripper, your friendly travel clothing planner. "
                + "I’m here to help you pack perfectly for your adventure!";
        assertEquals(expected01, conversationManager.getGreeting(userName01));
        assertEquals(expected02, conversationManager.getGreeting(userName02));
        assertEquals(expected03, conversationManager.getGreeting(userName03));
    }

    @Test
    void testAskForTripDetails() {
        String expected = "Could you please tell me a bit about your trip?";
        assertEquals(expected, conversationManager.askForTripDetails());
    }

    @Test
    void testFriendlyResponse() {
        String dynamicResponse = "Don't forget a rain jacket!";
        String expected = "Great news! Based on your trip details, here's what I recommend:\n"
                + dynamicResponse;
        assertEquals(expected, conversationManager.friendlyResponse(dynamicResponse));
    }
}
