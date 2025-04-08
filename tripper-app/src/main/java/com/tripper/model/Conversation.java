package com.tripper.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String userId;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Setter
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    // Getters and setters
    public Long getId() { return id; }

    public String getUserId() { return userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Message> getMessages() { return messages; }
}
