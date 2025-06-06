package com.tripper.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Message is an entity class that represents a message in the system.
 * It contains information about the message, including its ID, user ID,
 * sender, content, timestamp, and the conversation it belongs to.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
@Getter
@Entity
public class Message {

    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String userId;

    @Setter
    private String sender;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;

    private final LocalDateTime timestamp = LocalDateTime.now();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonBackReference
    private Conversation conversation;

}
