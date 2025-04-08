package com.tripper.service;

import com.tripper.model.Conversation;
import com.tripper.model.Message;
import com.tripper.repository.ConversationRepository;
import com.tripper.client.ChatGPTClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripChatService {

    @Autowired
    private ChatGPTClient chatGPTClient;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ConversationRepository conversationRepository;


    public String chatWithGPT(Long conversationId) {

        List<Message> messages = conversationService.getConversationMessages(conversationId);

        String prompt = buildPromptFromMessages(messages);

        String reply = chatGPTClient.getChatResponse(prompt);

        return reply;
    }

    private String buildPromptFromMessages(List<Message> messages) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are Tripper, a helpful travel planner. Respond in a warm and friendly tone.\n");

        for (Message message : messages) {
            if (message.getSender().equalsIgnoreCase("user")) {
                prompt.append("User: ").append(message.getContent()).append("\n");
            } else {
                prompt.append("Assistant: ").append(message.getContent()).append("\n");
            }
        }

        return prompt.toString();
    }
}
