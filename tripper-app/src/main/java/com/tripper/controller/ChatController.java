package com.tripper.controller;

import com.tripper.model.Conversation;
import com.tripper.model.Message;
import com.tripper.repository.MessageView;
import com.tripper.service.ConversationService;
import com.tripper.service.TripChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * ChatController handles all chat-related operations, including starting conversations, sending messages, and retrieving conversation history.
 * 
 * It provides endpoints for: 
 * 1. Starting a new conversation
 * 2. Sending messages to the assistant and receiving replies
 * 3. Retrieving message history for a conversation
 * 4. Listing all conversations for a user
 * 5. Updating conversation titles
 * 6. Deleting conversations
 * 7. Exporting conversations as PDF files
 * 
 * This controller uses the ConversationService to manage conversations and the TripChatService to interact with the ChatGPT API.
 * 
 * @author vitorlfreitas
 * @version 1.0.1
 */
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    // Default user ID for anonymous users
    private static final String DEFAULT_USER_ID = "anonymous";

    private final ConversationService conversationService;
    private final TripChatService tripChatService;

    /**
     * Start a new conversation.
     * This method initializes a new conversation for the user and returns the conversation ID.
     * 
     * @param request contains the user ID (optional, defaults to "anonymous")
     * @return a map containing the conversation ID
     * @throws IllegalArgumentException if the user ID is invalid
     * @throws NullPointerException if the user ID is null
     * @throws Exception if an error occurs while processing the request
     * 
     * @see ConversationService
     * @see TripChatService
     * @see Conversation
     * @see Message
     * @see MessageView
     * 
     * @author vitorlfreitas
     * @version 1.0.1
     */
    @PostMapping("/start")
    public Map<String, Object> startConversation(@RequestBody Map<String, String> request) {

        String userId = request.getOrDefault("userId", DEFAULT_USER_ID);

        Conversation convo = conversationService.startNewConversation(userId);
        
        return Collections.singletonMap("conversationId", convo.getId());
    }

    /**
     * Send a message to the assistant and receive a reply.
     * This method handles the chat interaction between the user and the assistant.
     * It saves the user's message, calls the ChatGPT API via TripChatService,
     * and saves the assistant's reply.
     * 
     * @param conversationId
     * @param payload
     * @return List of MessageView objects representing the updated chat history
     * @throws IllegalArgumentException if the conversation ID is invalid or the message is empty
     * @throws NullPointerException if the user ID is null
     * @throws Exception if an error occurs while processing the request
     * 
     * @see ConversationService
     * @see TripChatService
     * @see MessageView
     * @see Message
     * @see Conversation
     * 
     * @author vitorlfreitas
     * @version 1.0.1
     */
    @PostMapping("/{conversationId}/message")
    public List<MessageView> chat(
            @PathVariable Long conversationId,
            @RequestBody Map<String, String> payload
    ) {

        String userId = payload.getOrDefault("userId", DEFAULT_USER_ID);
        String userMessage = payload.get("message");

        // Save user message
        conversationService.addMessage(conversationId, "user", userMessage, userId);

        // Call ChatGPT via TripChatService
        String botReply = tripChatService.chatWithGPT(conversationId);

        // Save bot message
        conversationService.addMessage(conversationId, "assistant", botReply, userId);

        // Return updated chat history
        return conversationService.getConversationMessages(conversationId);
    }

    /**
     * Get messages for a specific conversation.
     * This method retrieves the message history for a given conversation ID.
     * 
     * @param conversationId the ID of the conversation
     * @return List of MessageView objects representing the conversation messages
     * @throws IllegalArgumentException if the conversation ID is invalid
     * @throws NullPointerException if the conversation ID is null
     * @throws Exception if an error occurs while processing the request
     * 
     * @see ConversationService
     * @see MessageView
     * @see Message
     * @see Conversation
     * 
     * @author vitorlfreitas
     * @version 1.0.1
     */
    @GetMapping("/{conversationId}/messages")
    public List<MessageView> getMessages(@PathVariable Long conversationId) {
        return conversationService.getConversationMessages(conversationId);
    }

    /**
     * Get all conversations for a user.
     * This method retrieves all conversations associated with a specific user ID.
     * 
     * @param userId the ID of the user
     * @return List of Conversation objects representing the user's conversations
     * @throws IllegalArgumentException if the user ID is invalid
     * @throws NullPointerException if the user ID is null
     * @throws Exception if an error occurs while processing the request
     * 
     * @see ConversationService
     * @see MessageView
     * @see Message
     * @see Conversation
     *
     * @author vitorlfreitas
     * @version 1.0.1
     */
    @GetMapping("/user/{userId}")
    public List<Conversation> getUserConversations(@PathVariable String userId) {
        return conversationService.getUserConversations(userId);
    }

    /**
     * Get messages for a specific user and conversation.
     * This method retrieves the message history for a given user ID and conversation ID.
     * 
     * @param userId the ID of the user
     * @param conversationId the ID of the conversation
     * @return List of Message objects representing the user's messages in the conversation
     * @throws IllegalArgumentException if the user ID or conversation ID is invalid
     * @throws NullPointerException if the user ID or conversation ID is null
     * @throws Exception if an error occurs while processing the request
     * 
     * @see ConversationService
     * @see MessageView
     * @see Message
     * @see Conversation
     * 
     * @author vitorlfreitas
     * @version 1.0.1
     */
    @GetMapping("/history")
    public List<Message> getUserMessages(
            @RequestParam String userId,
            @RequestParam Long conversationId
    ) {
        return conversationService.getMessagesByUserAndConversation(userId, conversationId);
    }

    /**
     * Update the title of a conversation.
     * This method allows the user to update the title of a specific conversation.
     * 
     * @param conversationId the ID of the conversation
     * @param payload contains the new title
     * @return ResponseEntity indicating the result of the operation
     * @throws IllegalArgumentException if the conversation ID is invalid or the title is empty
     * @throws NullPointerException if the conversation ID or title is null
     * @throws Exception if an error occurs while processing the request
     * 
     * @see ConversationService
     * @see MessageView
     * @see Message
     * @see Conversation
     * 
     * @author vitorlfreitas
     * @version 1.0.1
     */
    @PatchMapping("/{conversationId}/title")
    public ResponseEntity<?> updateConversationTitle(
            @PathVariable Long conversationId,
            @RequestBody Map<String, String> payload
    ) {
        // Validate the payload
        String newTitle = payload.get("title");
        conversationService.updateConversationTitle(conversationId, newTitle);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a conversation.
     * This method allows the user to delete a specific conversation.
     * 
     * @param conversationId the ID of the conversation
     * @return ResponseEntity indicating the result of the operation
     * @throws IllegalArgumentException if the conversation ID is invalid
     * @throws NullPointerException if the conversation ID is null
     * @throws Exception if an error occurs while processing the request
     * 
     * @see ConversationService
     * @see MessageView
     * @see Message
     * @see Conversation
     * 
     * @author vitorlfreitas
     * @version 1.0.1
     */
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<?> deleteConversation(@PathVariable Long conversationId) {
        conversationService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Export a conversation as a PDF file.
     * This method generates a PDF file containing the conversation history and returns it as a byte array.
     * 
     * @param conversationId the ID of the conversation
     * @return ResponseEntity containing the PDF file
     * @throws IllegalArgumentException if the conversation ID is invalid
     * @throws NullPointerException if the conversation ID is null
     * @throws Exception if an error occurs while processing the request
     * 
     * @see ConversationService
     * @see MessageView
     * @see Message
     * @see Conversation
     * 
     * @author vitorlfreitas
     * @version 1.0.1
     */
    @GetMapping("/{conversationId}/export/pdf")
    public ResponseEntity<byte[]> exportConversationAsPdf(@PathVariable Long conversationId) {
        try {
            Conversation conversation = conversationService.getConversationById(conversationId);
            String title = conversation.getTitle();
            if (title == null || title.trim().isEmpty()) {
                title = "conversation-" + conversationId;
            }

            // Sanitize the title to create a valid filename
            // Replace spaces and special characters with hyphens
            // Convert to lowercase
            // Example: "My Conversation Title!" -> "my-conversation-title-"
            String safeTitle = title.toLowerCase().replaceAll("[^a-z0-9]+", "-");

            // Generate the PDF file
            byte[] pdfBytes = conversationService.generateConversationPdf(conversationId);

            // Set the response headers for the PDF file
            HttpHeaders headers = new HttpHeaders();

            // Set the content type to PDF
            headers.setContentType(MediaType.APPLICATION_PDF);

            // Set the content disposition to attachment with the sanitized title
            headers.setContentDispositionFormData("attachment", safeTitle + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } 

        catch (Exception e) {
            // Return an error response if the PDF generation fails
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
