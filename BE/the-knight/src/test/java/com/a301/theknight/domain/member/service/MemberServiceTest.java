package com.a301.theknight.domain.member.service;

import com.a301.theknight.domain.member.dto.MemberInfoResponse;
import com.a301.theknight.domain.member.dto.MemberUpdateRequest;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    RankingRepository rankingRepository;

    static Member testMember;

    @BeforeAll
    static void beforeAll() {
        testMember = Member.builder()
                .nickname("테스트유저1")
                .image("imageURL")
                .email("test@naver.com")
                .password("password")
                .role("ROLE_USER").build();
    }

    @Test
    @DisplayName("회원 정보 조회, 랭킹 O")
    void memberInfoWithRanking() {
        Ranking ranking = Ranking.builder()
                .member(testMember).build();
        for (int i = 0; i < 4; i++) {
            ranking.saveWinScore();
        }
        ranking.saveLoseScore();

        MemberService memberService = new MemberService(memberRepository, rankingRepository);
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(rankingRepository.findByMemberId(1L)).willReturn(Optional.of(ranking));
        given(rankingRepository.findMemberRanking(1L)).willReturn(3);

        MemberInfoResponse memberInfo = memberService.getMemberInfo(1L);

        assertEquals("테스트유저1", memberInfo.getNickname());
        assertEquals("imageURL", memberInfo.getImage());
        assertEquals(3, memberInfo.getRanking());
        assertEquals(30, memberInfo.getScore());
        assertEquals(4, memberInfo.getWin());
        assertEquals(1, memberInfo.getLose());
    }

    @Test
    @DisplayName("회원 정보 조회, 랭킹 X")
    void memberInfoWithoutRanking() {
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(rankingRepository.findByMemberId(1L)).willReturn(Optional.empty());
        given(rankingRepository.findMemberRanking(1L)).willReturn(2);

        MemberService memberService = new MemberService(memberRepository, rankingRepository);
        MemberInfoResponse memberInfo = memberService.getMemberInfo(1L);

        assertEquals("테스트유저1", memberInfo.getNickname());
        assertEquals("imageURL", memberInfo.getImage());
        assertEquals(2, memberInfo.getRanking());
        assertEquals(0, memberInfo.getScore());
        assertEquals(0, memberInfo.getWin());
        assertEquals(0, memberInfo.getLose());
    }

    @Test
    @DisplayName("회원 정보 수정")
    void updateMemberInfo() {
        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setNickname("닉네임 변경");
        request.setImage("이미지 변경");

        Member member = Member.builder()
                .nickname("변경 전")
                .email("변경 전")
                .password("password")
                .image("변경 전").build();

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        MemberService memberService = new MemberService(memberRepository, rankingRepository);
        memberService.updateMemberInfo(1L, request);

        assertEquals("닉네임 변경", member.getNickname());
        assertEquals("이미지 변경", member.getImage());
    }
}