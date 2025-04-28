package com.tripper.repository;

import com.tripper.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * MessageRepository is an interface that extends JpaRepository to provide CRUD operations
 * for the Message entity. It includes methods to find messages by conversation ID and user ID.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<MessageView> findByConversationIdOrderByTimestampAsc(Long conversationId);

    List<Message> findByUserIdAndConversationId(String userId, Long conversationId);

    List<Message> findByConversationId(Long conversationId);


}
