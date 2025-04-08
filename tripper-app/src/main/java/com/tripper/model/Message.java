package com.tripper.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String sender; // "user" or "assistant"
    @Setter
    private String content;
    private LocalDateTime timestamp = LocalDateTime.now();

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    // Getters and setters
    public Long getId() { return id; }

    public String getSender() { return sender; }

    public String getContent() { return content; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public Conversation getConversation() { return conversation; }
}
