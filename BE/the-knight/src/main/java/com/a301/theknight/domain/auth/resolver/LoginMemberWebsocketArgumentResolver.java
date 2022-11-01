package com.a301.theknight.domain.auth.resolver;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.auth.model.MemberPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class LoginMemberWebsocketArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMemberIdAnnotation = parameter.getParameterAnnotation(LoginMemberId.class) != null;
        boolean isMemberIdClass = long.class.equals(parameter.getParameterType()) || Long.class.equals(parameter.getParameterType());

        return isLoginMemberIdAnnotation && isMemberIdClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Principal principal = accessor.getUser();
        return Long.valueOf(principal.getName());
    }
}
