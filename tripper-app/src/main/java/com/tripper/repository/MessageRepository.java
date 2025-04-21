package com.tripper.repository;

import com.tripper.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<MessageView> findByConversationIdOrderByTimestampAsc(Long conversationId);

    List<Message> findByUserIdAndConversationId(String userId, Long conversationId);

    List<Message> findByConversationId(Long conversationId);


}
