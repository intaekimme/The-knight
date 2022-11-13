package com.a301.theknight.domain.auth.service;

import com.a301.theknight.domain.auth.model.TokenSet;
import com.a301.theknight.domain.auth.util.TokenProperties;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {
    private final MemberRepository memberRepository;
    private final TokenProperties tokenProperties;
    private final Map<String, Long> blackList = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 600000)
    protected void clearBlackList() {
        blackList.forEach((token, id) -> {
            if (!validateToken(token, tokenProperties.getAccess().getName()))
                blackList.remove(token);
        });
    }

    public void setBlackList(String accessToken, Long memberId) {
        if (!StringUtils.hasText(accessToken) || memberId == null || memberId == 0)
            return;
        blackList.put(accessToken, memberId);
    }

    public boolean isInBlackList(String accessToken) {
        return blackList.get(accessToken) != null;
    }

    @Transactional
    public TokenSet issueNewToken(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomRestException(MemberErrorCode.MEMBER_IS_NOT_EXIST));

        String accessToken = createJwt(member, tokenProperties.getAccess().getName());
        String refreshToken = createJwt(member, tokenProperties.getRefresh().getName());
        log.info("  Issue New Token : Access-Token = {}, Refresh-Token = {}", accessToken, refreshToken);

        member.saveRefreshToken(refreshToken);
        return new TokenSet(accessToken, refreshToken);
    }

    public String createJwt(Member member, String tokenName) {
        //Header 부분 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Claims claims = Jwts.claims();  //나중에 서버에서 파싱해서 볼 데이터 입니다.
        claims.put("type", tokenName);  //문자도 가능하고
        claims.put("id", member.getId());
        Date now = new Date();
        long expiredDate = tokenName.equals(tokenProperties.getAccess().getName())
                ? tokenProperties.getAccess().getExpiredTimeMilli()
                : tokenProperties.getRefresh().getExpiredTimeMilli();

        return Jwts.builder()
                .setHeader(headers)
                .setClaims(claims) // 데이터를 넣어 줍니다
                .setIssuedAt(now)   // 토큰 발행 일자
                .setExpiration(new Date(now.getTime() + expiredDate)) // 만료 기간 입니다
                .signWith(SignatureAlgorithm.HS256, tokenProperties.getSecret()) // 암호화 알고리즘과 암복호화에 사용할 키를 넣어줍니다
                .compact(); // Token 생성
    }

    public boolean validateToken(String token, String tokenType) {
        if (!StringUtils.hasText(token) || !StringUtils.hasText(tokenType)) {
            return false;
        }
        try {
            Claims body = Jwts.parser().setSigningKey(tokenProperties.getSecret()).parseClaimsJws(token).getBody();
            String getType = (String) body.get("type");
            if (tokenType.equals(getType)) {
                return !tokenType.equals(tokenProperties.getAccess().getName()) || !isInBlackList(token);
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.info("JWT Signature is wrong.");
        }  catch (UnsupportedJwtException e) {
            log.info("JWT Token is unsupported.");
        } catch (IllegalArgumentException e) {
            log.info("JWT Token is wrong.");
        } catch (ExpiredJwtException e) {
            log.info("JWT Token is Expired");
            //TODO: 토큰 만료 시 리프레시 토큰으로 갱신 로직 추가
        }
        return false;
    }

    public Long getId(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(tokenProperties.getSecret())
                    .parseClaimsJws(token).getBody();
            Integer id = (Integer) claims.get("id");
            return Long.valueOf(id);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("JWT Signature is wrong.");
        }  catch (UnsupportedJwtException e) {
            log.info("JWT Token is unsupported.");
        } catch (IllegalArgumentException e) {
            log.info("JWT Token is wrong.");
        } catch (ExpiredJwtException e) {
            log.info("JWT Token is Expired");
        }
        return 0L;
    }

    public Claims getClaimsInToken(String token) {
        try {
            return Jwts.parser().setSigningKey(tokenProperties.getSecret())
                    .parseClaimsJws(token).getBody();
        } catch (SecurityException | MalformedJwtException e) {
            log.info("JWT Signature is wrong.");
        }  catch (UnsupportedJwtException e) {
            log.info("JWT Token is unsupported.");
        } catch (IllegalArgumentException e) {
            log.info("JWT Token is wrong.");
        } catch (ExpiredJwtException e) {
            log.info("JWT Token is Expired");
        }
        return null;
    }
}
