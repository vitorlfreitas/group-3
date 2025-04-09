package com.tripper.dto;

import com.tripper.model.Message;

public class OutgoingMessageDTO {
    private String sender;
    private String content;
    private String timestamp;

    public OutgoingMessageDTO(String sender, String content, String timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public static OutgoingMessageDTO from(Message message) {
        return new OutgoingMessageDTO(
                message.getSender(),
                message.getContent(),
                message.getTimestamp().toString()
        );
    }

    public String getSender() { return sender; }
    public String getContent() { return content; }
    public String getTimestamp() { return timestamp; }
}
