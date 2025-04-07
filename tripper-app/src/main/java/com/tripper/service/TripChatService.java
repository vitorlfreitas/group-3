package com.tripper.service;

import com.tripper.client.ChatGPTClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripChatService {

    @Autowired
    private ChatGPTClient chatGPTClient;

    public String generateTripAdvice(String name, List<String> locations, List<String> dates) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("User name: ").append(name).append("\n");
        prompt.append("Trip details:\n");
        prompt.append("- Locations: ").append(String.join(", ", locations)).append("\n");
        prompt.append("- Dates or months: ").append(String.join(", ", dates)).append("\n");

        prompt.append("Generate a friendly and detailed clothing packing list and travel tips for this trip. ");
        prompt.append("Include items based on the likely weather and destination types. Also suggest must-have accessories and optional items.");

        return chatGPTClient.getChatResponse(prompt.toString());
    }
}
