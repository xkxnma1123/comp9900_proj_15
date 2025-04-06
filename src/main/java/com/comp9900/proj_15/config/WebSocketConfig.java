package com.comp9900.proj_15.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register STOMP endpoint, clients will connect through here
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // Should restrict origins in production environment
                .withSockJS();  // Enable SockJS fallback options
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable simple in-memory message broker
        // Destination prefix for client messages
        registry.setApplicationDestinationPrefixes("/app");

        // Destination prefix for client subscriptions, removed /topic (used for broadcasting user status)
        registry.enableSimpleBroker("/queue", "/user");

        // Set prefix for user-specific messages
        registry.setUserDestinationPrefix("/user");
    }
}
