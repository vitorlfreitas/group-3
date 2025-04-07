package com.tripper.service;

import com.tripper.client.ChatGPTClient;
import com.tripper.client.WeatherService;
import com.tripper.model.TripChecklistSection;
import com.tripper.model.TripDetails;
import com.tripper.model.TripRequest;
import com.tripper.model.TripResponse;
import com.tripper.util.GPTChecklistParser;
import com.tripper.util.NLPInputParser;
import com.tripper.util.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripPlannerService {

    @Autowired
    private ChatGPTClient chatGPTClient;

    @Autowired
    private WeatherService weatherService;

    public TripResponse processTripRequest(TripRequest request) {
        // Initialize a TripResponse
        TripResponse response = new TripResponse();

        // 1) Parse user input (trip details) with NLP
        NLPInputParser nlpParser = new NLPInputParser();
        TripDetails tripDetails = nlpParser.parseTripDetails(request.getTripDetails());
        // (Optional) We don't do anything with tripDetails right now, but you could.

        // 2) (Optional) Enrich with weather data
        // if you want to use it in GPT context
        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            var weather = weatherService.getForecastData(request.getLocation());
            // You could pass weather data into conversationContext if you want
        }

        // 3) Build ChatGPT prompt with strict format instructions
        String conversationContext =
                "User trip details: " + request.getTripDetails() + "\n\n" +
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
        response.setRecommendations(dynamicResponse); // store raw GPT output

        // 5) Parse structured sections from GPT’s formatted response
        List<TripChecklistSection> structuredSections = GPTChecklistParser.parse(dynamicResponse);

        // 6) Generate PDF if requested
        if (request.isGeneratePdf()) {
            String pdfFileName = request.getUserName() + "_TripChecklist.pdf";
            PDFGenerator.generateStructuredChecklist(pdfFileName, request.getUserName(), structuredSections);
            response.setPdfFileName(pdfFileName);
        }

        // 7) Fill out basic response details
        response.setUserName(request.getUserName());

        return response;
    }
}
