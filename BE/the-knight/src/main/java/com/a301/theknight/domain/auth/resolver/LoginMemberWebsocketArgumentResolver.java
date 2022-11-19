package com.a301.theknight.domain.auth.resolver;

import com.a301.theknight.domain.auth.annotation.LoginMemberId;
import com.a301.theknight.domain.auth.model.MemberPrincipal;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
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

        if (principal == null) {
            log.info(" [Member is null in LoginWebsocketResolveArgument]");
            throw new CustomRestException(DomainErrorCode.DO_NOT_HAVE_AUTHENTICATION);
        }
        return Long.valueOf(principal.getName());
    }
}
