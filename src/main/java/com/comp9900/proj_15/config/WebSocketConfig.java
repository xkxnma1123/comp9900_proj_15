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
        // 注册STOMP端点，客户端将通过这里进行连接
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // 在生产环境中应该限制来源
                .withSockJS();  // 启用SockJS回退选项
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 启用简单的基于内存的消息代理
        // 客户端发送消息的目的地前缀
        registry.setApplicationDestinationPrefixes("/app");

        // 客户端订阅消息的目的地前缀，移除/topic（用于广播用户状态）
        registry.enableSimpleBroker("/queue", "/user");

        // 设置用于指定用户消息的前缀
        registry.setUserDestinationPrefix("/user");
    }
}
