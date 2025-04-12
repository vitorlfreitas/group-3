package com.tripper.model;

import lombok.Getter;
import lombok.Setter;

// Data Transfer Object (DTO) for transferring message data
@Setter
@Getter
public class MessageDTO {

    // Setter for conversationId
    // Getter for conversationId
    private Long conversationId; // Stores the ID of the conversation

    // Setter for userId
    // Getter for userId
    private String userId; // Stores the ID of the user

    // Setter for content
    // Getter for content
    private String content; // Stores the content of the message

}