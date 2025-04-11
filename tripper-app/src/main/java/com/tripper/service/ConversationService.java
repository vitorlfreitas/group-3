package com.tripper.service;

import com.tripper.model.Conversation;
import com.tripper.model.Message;
import com.tripper.repository.ConversationRepository;
import com.tripper.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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



}
