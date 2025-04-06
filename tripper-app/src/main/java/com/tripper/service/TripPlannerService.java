package com.tripper.service;

import com.tripper.client.ChatGPTClient;
import com.tripper.client.WeatherService;
import com.tripper.model.TripChecklist;
import com.tripper.model.TripDetails;
import com.tripper.model.TripRequest;
import com.tripper.model.TripResponse;
import com.tripper.util.NLPInputParser;
import com.tripper.util.PDFGenerator;
import com.tripper.util.TripChecklistGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripPlannerService {

    @Autowired
    private ChatGPTClient chatGPTClient;

    @Autowired
    private WeatherService weatherService;

    public TripResponse processTripRequest(TripRequest request) {
        TripResponse response = new TripResponse();

        // 1. Parse trip details (using your NLP parser)
        NLPInputParser nlpParser = new NLPInputParser();
        TripDetails tripDetails = nlpParser.parseTripDetails(request.getTripDetails());

        // 2. (Optional) Enrich with weather data if location provided
        if (request.getLocation() != null && !request.getLocation().isEmpty()) {
            var weather = weatherService.getForecastData(request.getLocation());
            // For demonstration, you might extract weather details and attach them to tripDetails
            // For example: tripDetails.setWeatherSummary(...);
        }

        // 3. Generate recommendations via ChatGPT API
        String conversationContext = "User trip details: " + request.getTripDetails() + "\n"
                + "Provide detailed clothing recommendations for this trip, considering the season and local weather conditions.";
        String dynamicResponse = chatGPTClient.getChatResponse(conversationContext);
        response.setRecommendations(dynamicResponse);

        // 4. Generate checklist arrays using TripChecklistGenerator
        TripChecklist checklist = TripChecklistGenerator.generateChecklist(tripDetails);
        response.setEssentialItems(checklist.getEssentialItems());
        response.setRecommendationsList(checklist.getRecommendations());
        response.setOptionalItems(checklist.getOptionalItems());
        response.setAccessories(checklist.getAccessories());

        // 5. Optionally generate a PDF checklist if requested
        if (request.isGeneratePdf()) {
            String pdfFileName = request.getUserName() + "_TripChecklist.pdf";
            PDFGenerator.generateChecklist(pdfFileName, request.getUserName(), request.getTripDetails(),
                    checklist.getEssentialItems(),
                    checklist.getRecommendations(),
                    checklist.getOptionalItems(),
                    checklist.getAccessories());
            response.setPdfFileName(pdfFileName);
        }

        response.setUserName(request.getUserName());
        return response;
    }
}
