package com.tripper.repository;

import com.tripper.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ConversationRepository is an interface that extends JpaRepository to provide CRUD operations
 * for the Conversation entity. It includes a method to find conversations by user ID.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserId(String userId);
}
