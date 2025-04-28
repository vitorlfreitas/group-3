package com.tripper.controller;

import com.tripper.dto.OutgoingMessageDTO;
import com.tripper.mapper.MessageMapper;
import com.tripper.dto.MessageDTO;
import com.tripper.repository.MessageView;
import com.tripper.service.ConversationService;
import com.tripper.service.TripChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * This class handles WebSocket communication for chat functionality.
 * It listens for incoming messages and sends replies back to the client.
 * The class is annotated with @Controller, making it a Spring MVC controller.
 * The handleChat method is invoked when a message is sent to the "/chat.send" destination.
 * It processes the incoming message, interacts with the conversation service,
 * and sends the response back to the client using SimpMessagingTemplate.
 *
 * @see ConversationService
 * @see TripChatService
 * @see SimpMessagingTemplate
 * @see MessageMapper
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ConversationService conversationService;
    private final TripChatService tripChatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;

    @MessageMapping("/chat.send") // Maps incoming WebSocket messages to this method
    public void handleChat(@Payload MessageDTO incomingMessage) {

        // Extract conversation ID from the incoming message
        Long conversationId = incomingMessage.conversationId();

        // Extract user ID from the incoming message
        String userId = incomingMessage.userId();

        // Extract user message content from the incoming message
        String userContent = incomingMessage.content();

        // Save the user's message to the conversation
        conversationService.addMessage(conversationId, "user", userContent, userId);

        // Generate a reply from GPT based on the conversation history
        String reply = tripChatService.chatWithGPT(conversationId);
        System.out.println("Assistant Reply: " + reply);

        // Save the assistant's reply to the conversation
        conversationService.addMessage(conversationId, "assistant", reply, userId);

        // Fetch the full updated conversation history
        List<MessageView> messages = conversationService.getConversationMessages(conversationId);
        System.out.println("Messages from DB: " + messages);

        /*
         * Convert the list of messages to DTOs
         * This is necessary for sending the messages over WebSocket
         * as the front-end expects data in a specific format
         * defined by the OutgoingMessageDTO class.
         */
        List<OutgoingMessageDTO> dtoMessages = messageMapper.toDtoList(messages);

        // Debugging
        System.out.println("Mapped DTO messages: " + dtoMessages);


        messagingTemplate.convertAndSend("/topic/chat/" + conversationId, dtoMessages);
    }
}