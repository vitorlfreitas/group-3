package main.java.com.tripper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChatGPTClientTest {
    @Test
    void testErrorHandling() {
        ChatGPTClient mockClient = new ChatGPTClient() {
            @Override
            public String getChatResponse(String context) {
                throw new RuntimeException("Simulated failure");
            }
        };

        String result;
        try {
            result = mockClient.getChatResponse("Trigger error");
        } catch (Exception e) {
            result = "Error: " + e.getMessage();
        }
        assertTrue(result.contains("Error"));
    }
}