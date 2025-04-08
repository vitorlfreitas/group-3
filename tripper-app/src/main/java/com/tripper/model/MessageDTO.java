package com.tripper.model;

// Data Transfer Object (DTO) for transferring message data
public class MessageDTO {
    private Long conversationId; // Stores the ID of the conversation
    private String userId; // Stores the ID of the user
    private String content; // Stores the content of the message

    // Getter for conversationId
    public Long getConversationId() { return conversationId; }

    // Setter for conversationId
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }

    // Getter for userId
    public String getUserId() { return userId; }

    // Setter for userId
    public void setUserId(String userId) { this.userId = userId; }

    // Getter for content
    public String getContent() { return content; }

    // Setter for content
    public void setContent(String content) { this.content = content; }
}