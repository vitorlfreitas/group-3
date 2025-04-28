package com.tripper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Conversation is an entity class that represents a conversation in the system.
 * It contains information about the conversation, including its ID, user ID,
 * creation time, messages, start time, and title.
 *
 * @author vitorlfreitas
 * @version 1.0.1
 */
@Getter
@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String userId;

    final private LocalDateTime createdAt = LocalDateTime.now();

    @Setter
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;

    @Setter
    @Column(nullable = false)
    private LocalDateTime startedAt = LocalDateTime.now();

    @Setter
    @Column
    private String title;


}
