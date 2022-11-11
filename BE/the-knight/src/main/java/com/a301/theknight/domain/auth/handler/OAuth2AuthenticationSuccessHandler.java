package com.a301.theknight.domain.auth.handler;

import com.a301.theknight.domain.auth.model.TokenSet;
import com.a301.theknight.domain.auth.service.TokenService;
import com.a301.theknight.domain.auth.util.CookieUtils;
import com.a301.theknight.domain.auth.util.TokenProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.a301.theknight.domain.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final TokenProperties tokenProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        TokenSet tokenSet = tokenService.issueNewToken(email);
        long memberId = tokenService.getId(tokenSet.getAccess());

        String targetUrl = makeRedirectUrl(request, tokenSet.getAccess(), memberId);
        if (response.isCommitted()) {
            log.debug("Response is already committed. Can't redirect {}", targetUrl);
            return;
        }

        String refreshTokenName = tokenProperties.getRefresh().getName();
        CookieUtils.addCookie(response, refreshTokenName,
                tokenSet.getRefresh(), (int) tokenProperties.getRefresh().getExpiredTime());

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String makeRedirectUrl(HttpServletRequest request, String token, long memberId) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .queryParam("memberId", memberId)
                .build().toUriString();
    }

}
