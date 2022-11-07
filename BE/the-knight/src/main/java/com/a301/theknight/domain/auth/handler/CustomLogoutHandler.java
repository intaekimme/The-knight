package com.a301.theknight.domain.auth.handler;

import com.a301.theknight.domain.auth.util.TokenProperties;
import com.a301.theknight.domain.auth.service.TokenService;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenService tokenService;
    private final TokenProperties tokenProperties;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //request의 헤더에서 토큰 필터링 & 검증
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);
            //토큰이 유효하면 해당 Access토큰을 블랙리스트에 저장함
            if (tokenService.validateToken(accessToken, tokenProperties.getAccess().getName())) {
                Long memberId = tokenService.getId(accessToken);
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new CustomRestException(MemberErrorCode.MEMBER_IS_NOT_EXIST));
                member.removeRefreshToken();
                tokenService.setBlackList(accessToken, memberId);
            }
        }
        //TODO: 토큰이 유효하지 않으면?? 401에러??
    }
}
