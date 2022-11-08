package com.a301.theknight.domain.auth.service;

import com.a301.theknight.domain.auth.model.MemberPrincipal;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.global.error.errorcode.MemberErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new CustomRestException(MemberErrorCode.MEMBER_IS_NOT_EXIST));

        return MemberPrincipal.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .build();
    }

    public UserDetails loadMemberById(Long id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomRestException(MemberErrorCode.MEMBER_IS_NOT_EXIST));

        return MemberPrincipal.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .build();
    }

}
