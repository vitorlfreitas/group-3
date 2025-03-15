package main.java.com.tripper;

public class ConversationManager {

    public String getGreeting(String userName) {
        return "Hello " + userName + "! I'm Tripper, your friendly travel clothing planner. "
                + "I’m here to help you pack perfectly for your adventure!";
    }

    public String askForTripDetails() {
        return "Could you please tell me a bit about your trip?";
    }

    public String friendlyResponse(String dynamicResponse) {
        return "Great news! Based on your trip details, here's what I recommend:\n"
                + dynamicResponse;
    }
}
