package com.a301.theknight.domain.member.service;

import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.member.dto.GameHistoryDto;
import com.a301.theknight.domain.member.dto.MemberHistoryResponse;
import com.a301.theknight.domain.member.dto.MemberInfoResponse;
import com.a301.theknight.domain.member.dto.MemberUpdateRequest;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.member.repository.MemberRepository;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
@Disabled
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    RankingRepository rankingRepository;

    @Mock
    PlayerRepository playerRepository;

    MemberService memberService;

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

    @BeforeEach
    void beforeEach() {
        memberService = new MemberService(memberRepository, rankingRepository, playerRepository);
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

        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));
        given(rankingRepository.findByMemberId(1L)).willReturn(Optional.of(ranking));
        given(rankingRepository.findMemberRanking(1L)).willReturn(3L);

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
        given(rankingRepository.findMemberRanking(1L)).willReturn(2L);

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

        memberService.updateMemberInfo(1L, request);

        assertEquals("닉네임 변경", member.getNickname());
        assertEquals("이미지 변경", member.getImage());
    }

    @Test
    @DisplayName("이전 플레이 기록 / 응답 테스트")
    void getMemberHistory1() {
        List<Player> players = new ArrayList<>();
        Game game = Game.builder()
                .title("game1")
                .capacity(10)
                .sword(4).twin(1).shield(3).hand(2).build();

        Member member = Member.builder()
                .nickname("변경 전")
                .email("변경 전")
                .password("password")
                .image("변경 전").build();

        Player player1 = Player.builder().member(testMember).game(game).build();
        Player player2 = Player.builder().member(member).game(game).build();
        player1.selectTeam(Team.A);
        player2.selectTeam(Team.B);

        player1.winGame();
        player2.loseGame();
        players.add(player1);

        given(playerRepository.findTenByMemberId(1L)).willReturn(players);

        MemberHistoryResponse memberHistory = memberService.getMemberHistory(1L);

        List<GameHistoryDto> games = memberHistory.getGames();
        assertEquals(1, games.size());

        GameHistoryDto gameHistoryDto = games.get(0);
        assertEquals(0, gameHistoryDto.getGameId());
        assertEquals(1, gameHistoryDto.getAlliance().size());
        assertEquals(testMember.getNickname(), gameHistoryDto.getAlliance().get(0).getNickname());
        assertEquals(1, gameHistoryDto.getOpposite().size());
        assertEquals(member.getNickname(), gameHistoryDto.getOpposite().get(0).getNickname());
        assertEquals(4, gameHistoryDto.getSword());
    }

}