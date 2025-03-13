package main.java.com.tripper;

import java.util.Scanner;

public class TerminalChatbot{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConversationManager conversationManager = new ConversationManager();
        ChatGPTClient chatGPTClient = new ChatGPTClient();
        ConversationState state = new ConversationState();

        // Multi-turn conversation loop

        // Step 1: Get user's name
        System.out.println("Welcome to Tripper Chatbot!");
        while (state.getUserName() == null || state.getUserName().isEmpty()) {
            System.out.print("Please enter your name: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                state.setUserName(name);
            } else {
                System.out.println("Oops! I didn't catch that. Please enter a valid name.");
            }
        }
        System.out.println(conversationManager.getGreeting(state.getUserName()));

        // Step 2: Ask for trip details
        System.out.println(conversationManager.askForTripDetails());
        String tripDetails = scanner.nextLine().trim();
        state.setTripDetails(tripDetails);

        // Step 3: Confirm trip details
        System.out.println(conversationManager.confirmTripDetails(tripDetails));
        String confirmation = scanner.nextLine().trim();
        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("No worries! Let's try again. " + conversationManager.askForTripDetails());
            tripDetails = scanner.nextLine().trim();
            state.setTripDetails(tripDetails);
        }
        state.setDetailsConfirmed(true);

        // Step 4: Build conversation context and generate recommendation
        String conversationContext = "User trip details: " + state.getTripDetails() + "\n"
                + "Provide detailed clothing recommendations, considering the season (e.g., summer in June if mentioned) and local weather conditions. "
                + "Include suggestions on quantities and accessories";
        String dynamicResponse = chatGPTClient.getChatResponse(conversationContext);
        System.out.println("Tripper: " + conversationManager.friendlyResponse(dynamicResponse));

        // Step 5: Ask for PDF checklist
        System.out.print("Would you like a PDF checklist with these recommendations? (yes/no): ");
        String pdfAnswer = scanner.nextLine().trim();
        if (pdfAnswer.equalsIgnoreCase("yes")) {

            // Simulate generating a PDF checklist using Apache PDFBox
            String checklistContent = "Clothing Checklist:\n"
                    + "- Waterproof jacket\n"
                    + "- Umbrella\n"
                    + "- 1 pair of trousers\n"
                    + "- 5 T-shirts\n"
                    + "- Optional: scarf and accessories\n";


            String pdfFileName = state.getUserName() + "_TripChecklist.pdf";
            PDFGenerator.generateChecklist(pdfFileName, checklistContent);

            System.out.println("Tripper: I've sent your PDF checklist! (File: " + pdfFileName + ")");

        } else {
            System.out.println("Tripper: Alright! Let me know if you need anything else.");
        }

        System.out.println("Thank you for chatting with me, " + state.getUserName() + "! Have a fantastic trip!");

        // Close the scanner
        scanner.close();
    }
}
