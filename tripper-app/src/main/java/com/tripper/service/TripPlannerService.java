package com.tripper.service;

import com.tripper.client.ChatGPTClient;
import com.tripper.client.WeatherService;
import com.tripper.dto.TripResponse;
import com.tripper.model.*;
import com.tripper.util.GPTChecklistParser;
import com.tripper.util.NLPInputParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripPlannerService {

    private final ChatGPTClient chatGPTClient;
    private final WeatherService weatherService;
    private final NLPInputParser nlpParser;

    public TripResponse processTripRequest(TripRequest request) {

        // 1) Parse input
        TripDetails details = nlpParser.parseTripDetails(request.getTripDetails());
        List<String> cities = details.getLocations();

        // 2) Build weather section
        StringBuilder weatherSection = new StringBuilder();
        for (String city : cities) {

            Optional<WeatherResponse> maybeW = weatherService.getForecastData(city);
            if (maybeW.isEmpty()) {
                continue;
            }

            WeatherResponse w = maybeW.get();
            if (w.getList() == null || w.getList().isEmpty()) {
                continue;
            }

            var f = w.getList().get(0);
            weatherSection
                    .append("Based on the weather in ")
                    .append(w.getCity().getName()).append(": ")
                    .append(f.getMain().getTemp()).append("°C, ")
                    .append(f.getWeather().get(0).getDescription())
                    .append("\n\n");
        }

        String weatherInfo = weatherSection.toString();

        // 3) Build ChatGPT prompt with strict format instructions
        String conversationContext =
                "User trip details: " + request.getTripDetails() + "\n\n" +
                        weatherInfo +
                        "Please output a structured travel recommendation in the following format, for each city involved:\n" +
                        "```\n" +
                        "**CITY NAME**\n" +
                        "**Weather:**\n" +
                        "- item\n" +
                        "**Clothing:**\n" +
                        "- item\n" +
                        "**Accessories:**\n" +
                        "- item\n" +
                        "**Optional Items:**\n" +
                        "- item\n" +
                        "```\n" +
                        "No extra text or paragraphs after these headings. End with 'Safe travels!'";

        // 4) Call ChatGPT to get dynamic travel recommendations
        String dynamicResponse = chatGPTClient.getChatResponse(conversationContext);

        // 5) Parse structured sections from GPT’s formatted response
        List<TripChecklistSection> structuredSections = GPTChecklistParser.parse(dynamicResponse);

        // Build and return the record in one call
        return new TripResponse(
                request.getUserName(),
                dynamicResponse,
                structuredSections,
                null
        );
    }
}
