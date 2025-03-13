package main.java.com.tripper;

import java.util.Scanner;

public class ConversationController {
    private ConversationState state;
    private ConversationManager conversationManager;
    private ChatGPTClient chatGPTClient;
    private Scanner scanner;

    // Define our conversation states.
    private enum State {
        GREETING,
        COLLECT_TRIP_DETAILS,
        CONFIRM_DETAILS,
        GENERATE_RECOMMENDATIONS,
        OFFER_PDF,
        END
    }

    public ConversationController() {
        state = new ConversationState();
        conversationManager = new ConversationManager();
        chatGPTClient = new ChatGPTClient();
        scanner = new Scanner(System.in);
    }

    public void run() {
        State currentState = State.GREETING;

        while (currentState != State.END) {
            switch (currentState) {
                case GREETING:
                    typePrint("Welcome to Tripper Chatbot!", 0);
                    String name = prompt("Please enter your name: ");
                    if (!name.isEmpty()) {
                        state.setUserName(name);
                        typePrint(conversationManager.getGreeting(name), 0);
                        currentState = State.COLLECT_TRIP_DETAILS;
                    } else {
                        typePrint("Oops! I didn't catch that. Please try again.", 0);
                    }
                    break;

                case COLLECT_TRIP_DETAILS:
                    String tripDetails = prompt(conversationManager.askForTripDetails());
                    if (!tripDetails.isEmpty()) {
                        state.setTripDetails(tripDetails);
                        currentState = State.CONFIRM_DETAILS;
                    } else {
                        typePrint("Trip details cannot be empty. Please try again.", 0);
                    }
                    break;

                case CONFIRM_DETAILS:
                    typePrint("Reasoning...", 0);
                    state.setDetailsConfirmed(true);
                    currentState = State.GENERATE_RECOMMENDATIONS;
                    break;

                case GENERATE_RECOMMENDATIONS:
                    String conversationContext = "User trip details: " + state.getTripDetails() + "\n"
                            + "Provide detailed clothing recommendations for this trip, considering the season (e.g., summer if mentioned) "
                            + "and local weather conditions. Suggest specific items, quantities, and accessories, and mention must-do activities.";
                    String dynamicResponse = chatGPTClient.getChatResponse(conversationContext);
                    typePrint("Tripper: " + conversationManager.friendlyResponse(dynamicResponse), 5);
                    currentState = State.OFFER_PDF;
                    break;

                case OFFER_PDF:
                    String pdfAnswer = prompt("Would you like a PDF checklist with these recommendations? (yes/no): ");
                    if (pdfAnswer.equalsIgnoreCase("yes")) {
                        // For demonstration, we use static arrays; later, integrate with TripChecklistGenerator.
                        String pdfFileName = state.getUserName() + "_TripChecklist.pdf";
                        String[] essentialItems = {"Light T-Shirt", "Shorts", "Comfortable Walking Shoes", "Sunglasses"};
                        String[] recommendations = {"Hat", "Sunscreen", "Umbrella (in case of rain)"};
                        String[] optionalItems = {"Light Sweater", "Extra Pair of Socks"};
                        String[] accessories = {"Crossbody Bag", "Travel Adapter", "Power Bank"};
                        PDFGenerator.generateChecklist(pdfFileName, state, state.getTripDetails(),
                                essentialItems, recommendations, optionalItems, accessories);
                        typePrint("Tripper: I've generated your PDF checklist! (File: " + pdfFileName + ")", 0);
                    } else {
                        typePrint("Tripper: Alright! Let me know if you need any further assistance.", 0);
                    }
                    currentState = State.END;
                    break;

                default:
                    currentState = State.END;
                    break;
            }
        }

        typePrint("Thank you for chatting with me, " + state.getUserName() + "! Have a fantastic trip!", 0);
        scanner.close();
    }

    // Helper method to simulate a typing effect
    private void typePrint(String message, int delayMs) {
        for (char c : message.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    // Helper method to prompt the user and get input
    private String prompt(String message) {
        typePrint(message, 0);
        return scanner.nextLine().trim();
    }
}
