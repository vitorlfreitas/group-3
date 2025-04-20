package com.tripper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * WebSocketConfig class for configuring WebSocket communication in the application.
 * This class implements WebSocketMessageBrokerConfigurer to customize WebSocket settings.
 * It registers STOMP endpoints and configures the message broker.
 *
 * @see WebSocketMessageBrokerConfigurer
 */
@Configuration // Marks this class as a configuration class
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers a WebSocket endpoint that clients will use to connect
        // Allows cross-origin requests from any origin and enables SockJS fallback options
        registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configures a simple in-memory message broker for topics (e.g., /topic)
        registry.enableSimpleBroker("/topic");
        // Sets the application destination prefix for messages sent from clients (e.g., /app)
        registry.setApplicationDestinationPrefixes("/app");
    }
}