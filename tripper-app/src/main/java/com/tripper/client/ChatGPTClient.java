package com.tripper.client;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * ChatGPTClient is a component that interacts with the OpenAI API to get responses from the ChatGPT model.
 * It sends conversation context to the API and receives a response based on the provided context.
 * The API key and URL are injected from application properties.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
@Component
public class ChatGPTClient {

    @Value("${openai.api.key}") private String API_KEY;

    @Value("${openai.api.url}") private String API_URL;

    public String getChatResponse(String conversationContext) {
        try {
            // Set up the connection to the OpenAI API
            // Using HttpURLConnection for HTTP POST request
            // Set the URL to the OpenAI API endpoint
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("OpenAI-Project", "proj_vyXo6DEEcCsHKyIFxkxV2NSt");

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
            return "Error: " + e.getMessage();
        }
    }

}
