package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class ConversationControllerTest {
    @Test
    void testConversationFlowWithoutRefactor() {
        // Simulate user input: name -> trip details -> "no" to PDF
        String input = String.join(System.lineSeparator(),
                "Maria",                           // name
                "I'm going to Brazil in summer",    // trip details
                "no"                                // no to PDF
        );

        ByteArrayInputStream testInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(testInput);
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOutput));

        ConversationController controller = new ConversationController();
        controller.run();

        String output = testOutput.toString();
        assertTrue(output.contains("Welcome to Tripper Chatbot!"));
        assertTrue(output.contains("Hello Maria!")); // Greeting confirmation
        assertTrue(output.contains("Parsed Trip Details")); // From NLPInputParser
        assertTrue(output.contains("Tripper: Great news!")); // Response line
        assertTrue(output.contains("Have a fantastic trip!")); // End

        System.setIn(System.in);
        System.setOut(System.out);
    }
}
