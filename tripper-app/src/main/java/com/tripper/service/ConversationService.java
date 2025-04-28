package com.tripper.service;

import com.tripper.model.Conversation;
import com.tripper.model.Message;
import com.tripper.repository.ConversationRepository;
import com.tripper.repository.MessageRepository;
import com.tripper.repository.MessageView;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ConversationService is a service class that provides methods to manage conversations and messages.
 * It includes methods to start a new conversation, get user conversations, add messages,
 * update conversation titles, delete conversations, and generate PDFs of conversations.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final PdfGeneratorService pdfGeneratorService;

    /**
     * Starts a new conversation for a user.
     * The conversation is initialized with the current timestamp and a default title.
     * The conversation is then saved to the database.
     * @param userId userId
     * @return The newly created Conversation object.
     */
    public Conversation startNewConversation(String userId) {

        Conversation conversation = new Conversation();

        conversation.setUserId(userId);
        conversation.setStartedAt(LocalDateTime.now());
        conversation.setTitle("New conversation");
        return conversationRepository.save(conversation);
    }

    /**
     * Retrieves all conversations for a user.
     * The conversations are fetched from the database using the user ID.
     * @param userId userId
     * @return A list of Conversation objects associated with the user.
     */
    public List<Conversation> getUserConversations(String userId) {
        return conversationRepository.findByUserId(userId);
    }

    /**
     * Retrieves all messages for a specific conversation.
     * The messages are fetched from the database using the conversation ID.
     * @param conversationId conversationId
     * @return A list of MessageView objects associated with the conversation.
     */
    @Cacheable("conversations")
    public List<MessageView> getConversationMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
    }

    /**
     * Adds a new message to a conversation.
     * The message is created with the provided sender, content, and user ID.
     * The message is then saved to the database.
     * @param conversationId conversationId
     * @param sender sender
     * @param content content
     * @param userId userId
     */
    @CacheEvict(value = "conversations", key = "#conversationId")
    public void addMessage(Long conversationId, String sender, String content, String userId) {

        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow();

        Message message = new Message();
        message.setSender(sender);
        message.setContent(content);
        message.setUserId(userId);
        message.setConversation(conversation);

        messageRepository.save(message);
    }

    /**
     * Retrieves all messages for a specific user and conversation.
     * The messages are fetched from the database using the user ID and conversation ID.
     * @param userId userId
     * @param conversationId conversationId
     * @return A list of Message objects associated with the user and conversation.
     */
    public List<Message> getMessagesByUserAndConversation(String userId, Long conversationId) {
        return messageRepository.findByUserIdAndConversationId(userId, conversationId);
    }

    /**
     * Updates the title of a conversation.
     * The conversation is fetched from the database using the conversation ID,
     * and its title is updated with the provided new title.
     * @param conversationId conversationId
     * @param newTitle New title for the conversation.
     */
    public void updateConversationTitle(Long conversationId, String newTitle) {

        Conversation convo = conversationRepository.getReferenceById(conversationId);

        convo.setTitle(newTitle);
        conversationRepository.save(convo);
    }

    /**
     * Deletes a conversation from the database.
     * The conversation is fetched from the database using the conversation ID,
     * then it is deleted.
     * @param conversationId conversationId
     */
    public void deleteConversation(Long conversationId) {
        conversationRepository.deleteById(conversationId);
    }

    /**
     * Generates a PDF document for a conversation.
     * The PDF is generated using the conversation title and the messages in the conversation.
     * @param conversationId conversationId
     * @return A byte array representing the generated PDF document.
     */
    public byte[] generateConversationPdf(Long conversationId) {

        Conversation conversation = conversationRepository.getReferenceById(conversationId);

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
            displayName = Arrays.stream(displayName.split("[\\-_]"))
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

    /**
     * Retrieves a conversation by its ID.
     * The conversation is fetched from the database using the conversation ID.
     * @param id conversationId
     * @return The Conversation object associated with the provided ID.
     */
    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found with id: " + id));
    }

}
