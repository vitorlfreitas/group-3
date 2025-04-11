package com.tripper.service;

import com.tripper.model.Conversation;
import com.tripper.model.Message;
import com.tripper.repository.ConversationRepository;
import com.tripper.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    // Create a new conversation for a user
    public Conversation startNewConversation(String userId) {

        Conversation conversation = new Conversation();

        conversation.setUserId(userId);
        conversation.setStartedAt(LocalDateTime.now());
        conversation.setTitle("New conversation");
        return conversationRepository.save(conversation);
    }

    // Get all conversations for a user
    public List<Conversation> getUserConversations(String userId) {
        return conversationRepository.findByUserId(userId);
    }

    // Get messages in a conversation
    public List<Message> getConversationMessages(Long conversationId) {
        Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
        return conversationOpt
                .map(messageRepository::findByConversationOrderByTimestampAsc)
                .orElse(List.of());
    }

    // Add a new message to a conversation
    public void addMessage(Long conversationId, String sender, String content, String userId) {

        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow();

        Message message = new Message();
        message.setSender(sender);
        message.setContent(content);
        message.setUserId(userId);
        message.setConversation(conversation);

        messageRepository.save(message);
    }

    public List<Message> getMessagesByUserAndConversation(String userId, Long conversationId) {
        return messageRepository.findByUserIdAndConversationId(userId, conversationId);
    }

    public void updateConversationTitle(Long conversationId, String newTitle) {
        Conversation convo = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        convo.setTitle(newTitle);
        conversationRepository.save(convo);
    }

    public void deleteConversation(Long conversationId) {
        conversationRepository.deleteById(conversationId);
    }

    public byte[] generateConversationPdf(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        List<Message> messages = messageRepository.findByConversationId(conversationId);

        // Default title fallback
        String title = conversation.getTitle();
        if (title == null || title.trim().isEmpty()) {
            title = "Conversation #" + conversation.getId();
        }

        // Generate a user-friendly display name
        String displayName = "User";
        if (conversation.getUserId() != null) {
            displayName = conversation.getUserId().split("@")[0];
            displayName = Arrays.stream(displayName.split("[\\.\\-_]"))
                    .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                    .collect(Collectors.joining(" "));
        }

        // Build content with Tripper and user's name
        StringBuilder content = new StringBuilder();
        for (Message msg : messages) {
            if ("assistant".equals(msg.getSender())) {
                content.append("Tripper: ").append(msg.getContent()).append("\n\n");
            } else {
                content.append(displayName).append(": ").append(msg.getContent()).append("\n\n");
            }
        }

        return pdfGeneratorService.generatePdfFromText(
                title,
                content.toString(),
                displayName
        );
    }


    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found with id: " + id));
    }

}
