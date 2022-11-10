package com.a301.theknight.domain.auth.interceptor;

import com.a301.theknight.domain.auth.model.MemberPrincipal;
import com.a301.theknight.domain.auth.service.CustomUserDetailsService;
import com.a301.theknight.domain.auth.util.TokenProperties;
import com.a301.theknight.domain.auth.service.TokenService;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final TokenService tokenService;
    private final TokenProperties tokenProperties;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        log.info("=====>> [Websocket Request] {}, Destination = {}", accessor.getCommand(), accessor.getHeader(StompHeaderAccessor.DESTINATION_HEADER));

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String accessToken = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7);
            log.info("  Request Access-Token = {}", accessToken);
            try {
                if (StringUtils.hasText(accessToken) && tokenService.validateToken(accessToken, tokenProperties.getAccess().getName())) {
                    Long id = tokenService.getId(accessToken);
                    UserDetails userDetails = customUserDetailsService.loadMemberById(id);
                    MemberPrincipal memberPrincipal = (MemberPrincipal) userDetails;
                    log.info("  Request Member Id = {}, Email = {}", memberPrincipal.getMemberId(), memberPrincipal.getEmail());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberPrincipal, null, memberPrincipal.getAuthorities());
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    accessor.setUser(authentication);
                }
            } catch (Exception e) {
                throw new CustomRestException(DomainErrorCode.DO_NOT_HAVE_AUTHENTICATION);
            }
        }
        return message;
    }
}