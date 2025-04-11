package com.tripper.service;

import com.tripper.client.ChatGPTClient;
import com.tripper.client.WeatherService;
import com.tripper.model.Message;
import com.tripper.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TripChatService {

    @Autowired
    private ChatGPTClient chatGPTClient;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private TripInfoExtractionService tripInfoExtractionService;

    @Autowired
    private WeatherService weatherService;

    public String chatWithGPT(Long conversationId) {

        List<Message> messages = conversationService.getConversationMessages(conversationId);
        String conversationText = buildPromptFromMessages(messages);

        String lastUserMessage = messages.stream()
                .filter(m -> "user".equalsIgnoreCase(m.getSender()))
                .reduce((first, second) -> second)
                .map(Message::getContent)
                .orElse("");

//        String lastUserLower = lastUserMessage.trim().toLowerCase();
//        boolean isGreeting = lastUserLower.matches("(?i)\\b(hi|hello|hey|howdy|greetings)\\b");

        Map<String, Object> extracted = tripInfoExtractionService.extract(lastUserMessage);
        List<String> locations = (List<String>) extracted.get("locations");
        List<String> dates = (List<String>) extracted.get("dates");

        String weatherInfo = "";
        if (locations != null && !locations.isEmpty()) {
            for (String city : locations) {
                WeatherResponse weatherResponse = weatherService.getForecastData(city);
                if (weatherResponse != null &&
                        weatherResponse.getCity() != null &&
                        weatherResponse.getList() != null &&
                        !weatherResponse.getList().isEmpty()) {

                    WeatherResponse.Forecast forecast = weatherResponse.getList().get(0);

                    weatherInfo = "Based on the weather in " + weatherResponse.getCity().getName() +
                            " (" + forecast.getMain().getTemp() + "°C, " +
                            forecast.getWeather().get(0).getDescription() + "), ";
                    break;
                }
            }
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are Tripper, a friendly and helpful travel assistant chatbot.\n\n");

        if (dates == null || dates.isEmpty()) {
            prompt.append("If the user provided the locations but didn't provide a date for the trip, kindly ask when they are planning to go.\n\n");
        }

        if (!weatherInfo.isEmpty()) {
            prompt.append("Include the weather info: ").append(weatherInfo).append("\n\n");
        }


        prompt.append("If the user has provided the location and dates of their trip, then provide helpful and personalized clothing suggestions and travel tips based on their trip.\n\n");


        prompt.append("Conversation so far:\n").append(conversationText).append("\n\n");
        prompt.append("Now respond accordingly.");

        return chatGPTClient.getChatResponse(prompt.toString());
    }

    private String buildPromptFromMessages(List<Message> messages) {
        StringBuilder prompt = new StringBuilder();
        for (Message msg : messages) {
            if ("user".equalsIgnoreCase(msg.getSender())) {
                prompt.append(msg.getContent()).append("\n");
            } else {
                prompt.append(msg.getContent()).append("\n");
            }
        }
        return prompt.toString();
    }
}
