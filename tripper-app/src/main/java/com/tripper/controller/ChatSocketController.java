package com.tripper.controller;

import com.tripper.dto.OutgoingMessageDTO;
import com.tripper.model.Message;
import com.tripper.model.MessageDTO;
import com.tripper.service.ConversationService;
import com.tripper.service.TripChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller // Marks this class as a Spring MVC Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ConversationService conversationService;
    private final TripChatService tripChatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send") // Maps incoming WebSocket messages to this method
    public void handleChat(@Payload MessageDTO incomingMessage) {

        // Extract conversation ID from the incoming message
        Long conversationId = incomingMessage.getConversationId();

        // Extract user ID from the incoming message
        String userId = incomingMessage.getUserId();

        // Extract user message content from the incoming message
        String userContent = incomingMessage.getContent();

        // Save the user's message to the conversation
        conversationService.addMessage(conversationId, "user", userContent, userId);

        // Generate a reply from GPT based on the conversation history
        String reply = tripChatService.chatWithGPT(conversationId);

        // Save the assistant's reply to the conversation
        conversationService.addMessage(conversationId, "assistant", reply, userId);

        // Fetch the full updated conversation history
        List<Message> messages = conversationService.getConversationMessages(conversationId);

        List<OutgoingMessageDTO> dtoMessages = messages.stream()
                .map(OutgoingMessageDTO::from)
                .toList();

        messagingTemplate.convertAndSend("/topic/chat/" + conversationId, dtoMessages);
    }
}