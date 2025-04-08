package com.tripper.controller;

import com.tripper.model.Message;
import com.tripper.model.MessageDTO;
import com.tripper.service.ConversationService;
import com.tripper.service.TripChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller // Marks this class as a Spring MVC Controller
public class ChatSocketController {

    @Autowired // Injects the ConversationService bean
    private ConversationService conversationService;

    @Autowired // Injects the TripChatService bean
    private TripChatService tripChatService;

    @Autowired // Injects the SimpMessagingTemplate bean for WebSocket messaging
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send") // Maps incoming WebSocket messages to this method
    public void handleChat(@Payload MessageDTO incomingMessage) {

        // Extract conversation ID from the incoming message
        Long conversationId = incomingMessage.getConversationId();

        // Extract user ID from the incoming message
        String userId = incomingMessage.getUserId();

        // Extract user message content from the incoming message
        String userContent = incomingMessage.getContent();

        // Save the user's message to the conversation
        conversationService.addMessage(conversationId, "user", userContent);

        // Generate a reply from GPT based on the conversation history
        String reply = tripChatService.chatWithGPT(conversationId);

        // Save the assistant's reply to the conversation
        conversationService.addMessage(conversationId, "assistant", reply);

        // Fetch the full updated conversation history
        List<Message> messages = conversationService.getConversationMessages(conversationId);

        // Send the updated conversation history to the WebSocket topic for this conversation
        messagingTemplate.convertAndSend("/topic/chat/" + conversationId, messages);
    }
}