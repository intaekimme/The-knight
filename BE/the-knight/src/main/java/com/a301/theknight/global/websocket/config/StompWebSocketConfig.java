package com.a301.theknight.global.websocket.config;

import com.a301.theknight.global.webmvc.properties.DomainProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Configuration
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /*
    변경 가능 사항
    1. handShake endpoint
    2. CORS path
     */
    private final DomainProperties domainProperties;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/websocket")
                .setAllowedOrigins(domainProperties.getLocal(), domainProperties.getMain())
                .withSockJS();
    }

    /*
    변경 가능한 사항
    1. 구독 prefix
    2. 발생 prefix
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub");
    }

}
