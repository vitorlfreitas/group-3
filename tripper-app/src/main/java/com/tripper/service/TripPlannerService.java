package com.tripper.service;

import com.tripper.client.ChatGPTClient;
import com.tripper.client.WeatherService;
import com.tripper.model.TripChecklistSection;
import com.tripper.model.TripDetails;
import com.tripper.model.TripRequest;
import com.tripper.model.TripResponse;
import com.tripper.util.GPTChecklistParser;
import com.tripper.util.NLPInputParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripPlannerService {

    private final ChatGPTClient chatGPTClient;
    private final WeatherService weatherService;
    private final NLPInputParser nlpParser;

    public TripResponse processTripRequest(TripRequest request) {
        // Initialize a TripResponse
        TripResponse response = new TripResponse();

        // 1) Parse input
        TripDetails details = nlpParser.parseTripDetails(request.getTripDetails());
        List<String> cities = details.getLocations();

        // 2) Build weather section
        String weatherInfo = "";
        if (request.getLocation() != null && !request.getLocation().isEmpty()) {

            StringBuilder weatherSection = new StringBuilder();
            for (String city : cities) {
                var w = weatherService.getForecastData(city);
                if (w != null && w.getList() != null && !w.getList().isEmpty()) {
                    var f = w.getList().get(0);
                    weatherSection
                            .append("Based on the weather in ").append(w.getCity().getName()).append(": ")
                            .append(f.getMain().getTemp()).append("°C, ")
                            .append(f.getWeather().get(0).getDescription())
                            .append("\n\n");
                }
            }
            weatherInfo += weatherSection.toString();
        }

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
        response.setRecommendations(dynamicResponse);

        // 5) Parse structured sections from GPT’s formatted response
        List<TripChecklistSection> structuredSections = GPTChecklistParser.parse(dynamicResponse);

        // 7) Fill out basic response details
        response.setUserName(request.getUserName());

        return response;
    }
}
