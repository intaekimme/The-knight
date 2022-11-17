package com.a301.theknight.domain.auth.service;

import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final RankingRepository rankingRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String nickname = email.substring(0, email.indexOf('@'));
        String image = (String) attributes.get("picture");
        log.info("  Login Google : name = {}, email = {}", nickname, email);

        Member findMember = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("  Execute join new member");
                    Member member = memberRepository.save(Member.builder()
                            .email(email)
                            .nickname(nickname)
                            .password(passwordEncoder.encode(email))
                            .role("ROLE_USER")
                            .image(image)
                            .build());
                    rankingRepository.save(Ranking.builder().member(member).build());
                    return member;
                });

        return oAuth2User;
    }

}
