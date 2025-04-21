package com.tripper.dto;

/**
 * DTO for incoming messages,
 * This class is used to send messages from the client to the server.
 * It contains the conversation ID, user ID, and content of the message.
 *
 * @param conversationId The ID of the conversation
 * @param userId        The ID of the user
 * @param content       The content of the message
 *
 * @author vitorlfreitas
 */
public record MessageDTO(
        Long conversationId,
        String userId,
        String content
) {}