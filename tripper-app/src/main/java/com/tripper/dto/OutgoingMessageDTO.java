package com.tripper.dto;

import com.tripper.model.Message;

/**
 * DTO for outgoing messages,
 * This class is used to send messages from the server to the client.
 * It contains the sender, content, and timestamp of the message.
 *
 * @param sender    The sender of the message
 * @param content   The content of the message
 * @param timestamp The timestamp of the message
 *
 * @author vitorlfreitas
 */
public record OutgoingMessageDTO(
        String sender,
        String content,
        String timestamp
) {
    public static OutgoingMessageDTO from(Message message) {
        return new OutgoingMessageDTO(
                message.getSender(),
                message.getContent(),
                message.getTimestamp().toString()
        );
    }
}
