package com.a301.theknight.global.websocket.config;

import com.a301.theknight.domain.auth.interceptor.StompHandler;
import com.a301.theknight.domain.auth.resolver.LoginMemberWebsocketArgumentResolver;
import com.a301.theknight.global.webmvc.properties.DomainProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Configuration
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final LoginMemberWebsocketArgumentResolver loginMemberWebsocketArgumentResolver;
    private final DomainProperties domainProperties;
    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/websocket")
                .setAllowedOrigins(domainProperties.getLocal(), domainProperties.getMain())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(0, loginMemberWebsocketArgumentResolver);
    }
}
