package com.tripper.repository;

import java.time.LocalDateTime;

/**
 * JPA projection for read‑only retrieval of a message.
 * Only the columns we actually need for the chat flow.
 */
public interface MessageView {

    String getSender();

    String getContent();

    LocalDateTime getTimestamp();
}
