package com.tripper.controller;

import com.tripper.model.Conversation;
import com.tripper.model.Message;
import com.tripper.service.ConversationService;
import com.tripper.service.TripChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private static final String DEFAULT_USER_ID = "anonymous";

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private TripChatService tripChatService;

    // Start a new conversation for a user
    @PostMapping("/start")
    public Map<String, Object> startConversation(@RequestBody Map<String, String> request) {

        String userId = request.getOrDefault("userId", DEFAULT_USER_ID);

        Conversation convo = conversationService.startNewConversation(userId);

        return Collections.singletonMap("conversationId", convo.getId());
    }

    // Send a user message and get assistant reply
    @PostMapping("/{conversationId}/message")
    public List<Message> chat(
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

    // Get message history for a conversation
    @GetMapping("/{conversationId}/messages")
    public List<Message> getMessages(@PathVariable Long conversationId) {
        return conversationService.getConversationMessages(conversationId);
    }

    // List all conversations for a user
    @GetMapping("/user/{userId}")
    public List<Conversation> getUserConversations(@PathVariable String userId) {
        return conversationService.getUserConversations(userId);
    }

    // Get messages by user ID and conversation ID
    @GetMapping("/history")
    public List<Message> getUserMessages(
            @RequestParam String userId,
            @RequestParam Long conversationId
    ) {
        return conversationService.getMessagesByUserAndConversation(userId, conversationId);
    }

    @PatchMapping("/{conversationId}/title")
    public ResponseEntity<?> updateConversationTitle(
            @PathVariable Long conversationId,
            @RequestBody Map<String, String> payload
    ) {
        String newTitle = payload.get("title");
        conversationService.updateConversationTitle(conversationId, newTitle);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<?> deleteConversation(@PathVariable Long conversationId) {
        conversationService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{conversationId}/export/pdf")
    public ResponseEntity<byte[]> exportConversationAsPdf(@PathVariable Long conversationId) {
        try {
            Conversation conversation = conversationService.getConversationById(conversationId);
            String title = conversation.getTitle();
            if (title == null || title.trim().isEmpty()) {
                title = "conversation-" + conversationId;
            }

            String safeTitle = title.toLowerCase().replaceAll("[^a-z0-9]+", "-");

            byte[] pdfBytes = conversationService.generateConversationPdf(conversationId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", safeTitle + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
