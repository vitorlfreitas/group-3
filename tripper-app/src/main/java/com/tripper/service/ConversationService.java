package com.tripper.service;

import com.tripper.model.Conversation;
import com.tripper.model.Message;
import com.tripper.repository.ConversationRepository;
import com.tripper.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    // Create a new conversation for a user
    public Conversation startNewConversation(String userId) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
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
    public Message addMessage(Long conversationId, String sender, String content) {
        Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
        if (conversationOpt.isEmpty()) throw new RuntimeException("Conversation not found");

        Message message = new Message();
        message.setConversation(conversationOpt.get());
        message.setSender(sender);
        message.setContent(content);
        return messageRepository.save(message);
    }
}
