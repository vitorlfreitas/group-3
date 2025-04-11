package com.tripper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
