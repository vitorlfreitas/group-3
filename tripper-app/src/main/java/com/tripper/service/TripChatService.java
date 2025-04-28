package com.tripper.service;

import com.tripper.client.ChatGPTClient;
import com.tripper.client.WeatherService;
import com.tripper.dto.TripInfo;
import com.tripper.model.WeatherResponse;
import com.tripper.repository.MessageView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * TripChatService is a service class that provides methods to interact with the ChatGPT API
 * for travel-related queries. It includes methods to chat with the GPT model and extract trip information.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
@Service
@RequiredArgsConstructor
public class TripChatService {

    private final ChatGPTClient chatGPTClient;
    private final ConversationService conversationService;
    private final TripInfoExtractionService tripInfoExtractionService;
    private final WeatherService weatherService;

    /**
     * chatWithGPT is a method that interacts with the ChatGPT API to provide travel-related suggestions
     * based on the user's conversation history. It extracts trip information, including locations and dates,
     * and generates a personalized response using the ChatGPT model.
     *
     * @param conversationId The ID of the conversation to retrieve messages from.
     * @return A string response from the ChatGPT model.
     */
    public String chatWithGPT(Long conversationId) {

        List<MessageView> messages = conversationService.getConversationMessages(conversationId);
        String conversationText = buildPromptFromMessages(messages);

        String lastUserMessage = messages.stream()
                .filter(m -> "user".equalsIgnoreCase(m.getSender()))
                .reduce((first, second) -> second)
                .map(MessageView::getContent)
                .orElse("");


        TripInfo info      = tripInfoExtractionService.extract(lastUserMessage);
        List<String> locations = info.locations();
        List<String> dates     = info.dates();


        String weatherInfo = "";

        for (String city : locations) {
            Optional<WeatherResponse> maybeResp = weatherService.getForecastData(city);

            if (maybeResp.isEmpty()) {
                continue;
            }

            WeatherResponse resp = maybeResp.get();
            if (resp.getCity() == null ||
                    resp.getList() == null ||
                    resp.getList().isEmpty()) {
                continue;
            }

            WeatherResponse.Forecast forecast = resp.getList().get(0);
            weatherInfo = String.format(
                    "Based on the weather in %s (%.1f°C, %s), ",
                    resp.getCity().getName(),
                    forecast.getMain().getTemp(),
                    forecast.getWeather().get(0).getDescription()
            );
            break;
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

    /**
     * buildPromptFromMessages is a helper method that constructs a prompt string
     * from a list of messages. It concatenates the content of each message into a single string.
     *
     * @param messages A list of MessageView objects representing the conversation history.
     * @return A string representing the concatenated content of the messages.
     */
    private String buildPromptFromMessages(List<MessageView> messages) {
        StringBuilder prompt = new StringBuilder();
        for (MessageView msg : messages) {
            prompt.append(msg.getContent()).append("\n");
        }
        return prompt.toString();
    }

}
