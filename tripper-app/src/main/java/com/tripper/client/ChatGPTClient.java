package com.tripper.client;

import com.google.gson.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Component
public class ChatGPTClient {

    @Value("${openai.api.key}")
    private String API_KEY;

    @Value("${openai.api.url}")
    private String API_URL;

    public String getChatResponse(String conversationContext) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Build JSON payload
            JsonObject payload = new JsonObject();
            payload.addProperty("model", "gpt-3.5-turbo");

            // Build the conversation messages array
            JsonArray messages = new JsonArray();

            // System message to set the role
            JsonObject systemMsg = new JsonObject();
            systemMsg.addProperty("role", "system");
            systemMsg.addProperty("content", "You are Tripper, a helpful travel clothing planner chatbot. Provide detailed and friendly recommendations based on travel dates and weather conditions.");
            messages.add(systemMsg);

            // User message with the conversation context
            JsonObject userMsg = new JsonObject();
            userMsg.addProperty("role", "user");
            userMsg.addProperty("content", conversationContext);
            messages.add(userMsg);

            payload.add("messages", messages);

            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
            }

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Parse the response JSON using Gson
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray choices = jsonResponse.getAsJsonArray("choices");
            if (!choices.isEmpty()) {
                JsonObject firstChoice = choices.get(0).getAsJsonObject();
                String reply = firstChoice.getAsJsonObject("message").get("content").getAsString();
                return reply.trim();
            } else {
                return "I'm sorry, I couldn't generate a response.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @PostConstruct
    public void printEnv() {
        System.getenv().forEach((k, v) -> {
            if (k.contains("OPENAI")) {
                System.out.println("🔍 ENV: " + k + " = " + v);
            }
        });
    }
}
