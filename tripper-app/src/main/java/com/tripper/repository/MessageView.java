package com.tripper.repository;

import java.time.LocalDateTime;

/**
 * MessageView is an interface that represents a view of a message.
 * It contains methods to retrieve the sender, content, and timestamp of the message.
 * 
 * @author vitorlfreitas
 * @version 1.0.1
 */
public interface MessageView {

    String getSender();
    String getContent();
    LocalDateTime getTimestamp();
}
