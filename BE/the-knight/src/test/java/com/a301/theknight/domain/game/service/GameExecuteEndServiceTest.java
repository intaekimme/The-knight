package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.end.GameEndDto;
import com.a301.theknight.domain.game.dto.end.PlayerWeaponDto;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameResult;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.entity.redis.TeamInfoData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.domain.player.repository.PlayerRepository;
import com.a301.theknight.domain.ranking.entity.Ranking;
import com.a301.theknight.domain.ranking.repository.RankingRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GameExecuteEndServiceTest {

    @InjectMocks
    GameExecuteEndService gameExecuteEndService;
    @Mock
    GameRedisRepository gameRedisRepository;
    @Mock
    GameRepository gameRepository;
    @Mock
    RankingRepository rankingRepository;
    @Mock
    PlayerRepository playerRepository;

    static Member testMember1, testMember2, testMember3, testMember4;
    static Ranking testRanking1, testRanking2, testRanking3, testRanking4;
    static Game testGame;
    static Player testPlayer1, testPlayer2, testPlayer3, testPlayer4;
    static InGame testInGame;
    static InGamePlayer testInGamePlayer1, testInGamePlayer2, testInGamePlayer3, testInGamePlayer4;
    static long memberId1 = 1L, memberId2 = 2L, memberId3 = 3L, memberId4 = 4L, gameId = 5L;

    @BeforeAll
    static void beforeAll() {
        testMember1 = Member.builder()
                .nickname("테스트멤버1")
                .image("image")
                .email("test1@naver.com")
                .password("password")
                .role("ROLE_USER").build();
        testMember2 = Member.builder()
                .nickname("테스트멤버2")
                .image("image")
                .email("test2@naver.com")
                .password("password")
                .role("ROLE_USER").build();
        testMember3 = Member.builder()
                .nickname("테스트멤버3")
                .image("image")
                .email("test3@naver.com")
                .password("password")
                .role("ROLE_USER").build();
        testMember4 = Member.builder()
                .nickname("테스트멤버4")
                .image("image")
                .email("test4@naver.com")
                .password("password")
                .role("ROLE_USER").build();

        testRanking1 = Ranking.builder().member(testMember1).build();
        testRanking2 = Ranking.builder().member(testMember2).build();
        testRanking3 = Ranking.builder().member(testMember3).build();
        testRanking4 = Ranking.builder().member(testMember4).build();

        testRanking1.saveWinScore();    // 20
        testRanking1.saveWinScore();
        testRanking2.saveWinScore();    // 5
        testRanking2.saveLoseScore();
        testRanking3.saveWinScore();    // 15
        testRanking3.saveWinScore();
        testRanking3.saveLoseScore();

        testGame = Game.builder()
                .title("테스트게임")
                .capacity(4)
                .sword(1)
                .twin(1)
                .shield(1)
                .hand(1).build();

        testInGame = InGame.builder()
                .gameStatus(GameStatus.EXECUTE)
                .currentAttackTeam(Team.A)
                .teamAInfo(TeamInfoData.builder()
                        .currentAttackIndex(0)
                        .orderList(new GameOrderDto[]{GameOrderDto.builder()
                                .memberId(memberId1).build(),
                                GameOrderDto.builder()
                                        .memberId(memberId2).build()})
                        .leaderId(memberId1).build())
                .teamBInfo(TeamInfoData.builder()
                        .currentAttackIndex(0)
                        .orderList(new GameOrderDto[]{GameOrderDto.builder()
                                .memberId(memberId3).build(),
                                GameOrderDto.builder()
                                        .memberId(memberId4).build()})
                        .leaderId(memberId3).build())
                .maxMemberNum(4)
//                .turnData()
                .requestCount(0)
                .doubtPassCount(0).build();

//        Player
        testPlayer1 = new Player(testMember1, testGame);
        testPlayer2 = new Player(testMember2, testGame);
        testPlayer3 = new Player(testMember3, testGame);
        testPlayer4 = new Player(testMember4, testGame);
        testPlayer3.selectTeam(Team.B);
        testPlayer4.selectTeam(Team.B);

//        InGamePlayer
        testInGamePlayer1 = InGamePlayer.builder()
                .memberId(memberId1)
                .team(testPlayer1.getTeam())
                .leftCount(0)
                .rightCount(0)
                .leftWeapon(Weapon.SWORD)
                .rightWeapon(Weapon.SHIELD)
                .order(0)
                .isDead(false)
                .isLeader(true).build();
        testInGamePlayer2 = InGamePlayer.builder()
                .memberId(memberId2)
                .team(testPlayer2.getTeam())
                .leftCount(0)
                .rightCount(0)
                .leftWeapon(Weapon.TWIN)
                .rightWeapon(Weapon.HAND)
                .order(1)
                .isDead(false)
                .isLeader(false).build();
        testInGamePlayer3 = InGamePlayer.builder()
                .memberId(memberId3)
                .team(testPlayer3.getTeam())
                .leftCount(0)
                .rightCount(0)
                .leftWeapon(Weapon.SWORD)
                .rightWeapon(Weapon.SHIELD)
                .order(0)
                .isDead(false)
                .isLeader(true).build();
        testInGamePlayer4 = InGamePlayer.builder()
                .memberId(memberId4)
                .team(testPlayer4.getTeam())
                .leftCount(0)
                .rightCount(0)
                .leftWeapon(Weapon.TWIN)
                .rightWeapon(Weapon.HAND)
                .order(1)
                .isDead(false)
                .isLeader(false).build();

    }

    @Test
    @DisplayName("게임 종료 테스트")
    void gameEnd() throws Exception {
        // 게임 관련된 데이터 만들기
        // given
        testInGamePlayer1.death();

        given(gameRedisRepository.getInGame(gameId)).willReturn(Optional.of(testInGame));
        given(gameRedisRepository.getInGamePlayer(gameId, memberId1)).willReturn(Optional.of(testInGamePlayer1));
        given(gameRedisRepository.getInGamePlayer(gameId, memberId2)).willReturn(Optional.of(testInGamePlayer2));
        given(gameRedisRepository.getInGamePlayer(gameId, memberId3)).willReturn(Optional.of(testInGamePlayer3));
        given(gameRedisRepository.getInGamePlayer(gameId, memberId4)).willReturn(Optional.of(testInGamePlayer4));

        given(gameRepository.findById(gameId)).willReturn(Optional.of(testGame));
        given(playerRepository.findByGameIdAndMemberId(gameId, memberId1)).willReturn(Optional.of(testPlayer1));
        given(playerRepository.findByGameIdAndMemberId(gameId, memberId2)).willReturn(Optional.of(testPlayer2));
        given(playerRepository.findByGameIdAndMemberId(gameId, memberId3)).willReturn(Optional.of(testPlayer3));
        given(playerRepository.findByGameIdAndMemberId(gameId, memberId4)).willReturn(Optional.of(testPlayer4));

        given(rankingRepository.findByMemberId(memberId1)).willReturn(Optional.of(testRanking1));
        given(rankingRepository.findByMemberId(memberId2)).willReturn(Optional.of(testRanking2));
        given(rankingRepository.findByMemberId(memberId3)).willReturn(Optional.of(testRanking3));
        given(rankingRepository.findByMemberId(memberId4)).willReturn(Optional.of(testRanking4));

        int[] scores = {30, 15, 5, 0};

        // 게임 종료 실행했을 때
        // when
        GameEndDto gameEndDto = gameExecuteEndService.gameEnd(gameId);
        System.out.println(gameEndDto.getEndResponseA().getLosingTeam());
        System.out.println(gameEndDto.getEndResponseB().getLosingTeam());

//        1. Game의 status가 END로 변경되는지 확인
//        2. ranking 사용자들 점수가 정확한 값인지를 확인
//        3. Player의 게임 결과가 이긴 팀은 WIN, 진 팀은 LOSE로 바뀌었는지 확인
//        4. gameEndDto 결과 확인
        // then

//        1. Game의 status가 END로 변경되는지 확인
        assertEquals(GameStatus.END, testGame.getStatus());
        // memberId를 이 클래스의 static 변수와 해야하는지 gameEndDto에 담긴 memberId와 해야하는지 모르겠음

        int i = 0;
//        2. ranking 사용자들 점수가 0이거나 바뀌었는지 확인
        for (PlayerWeaponDto player : gameEndDto.getEndResponseA().getPlayers()) {
            // 여기 get()써도 되나?
            assertEquals(scores[i++], rankingRepository.findByMemberId(player.getMemberId()).get().getScore());
        }

//        3. Player의 게임 결과가 이긴 팀은 WIN, 진 팀은 LOSE로 변경되는지 확인
        assertEquals(GameResult.LOSE, playerRepository.findByGameIdAndMemberId(gameId, memberId1).get().getResult());
        assertEquals(GameResult.LOSE, playerRepository.findByGameIdAndMemberId(gameId, memberId2).get().getResult());
        assertEquals(GameResult.WIN, playerRepository.findByGameIdAndMemberId(gameId, memberId3).get().getResult());
        assertEquals(GameResult.WIN, playerRepository.findByGameIdAndMemberId(gameId, memberId4).get().getResult());

//        4. gameEndDto 결과 확인
        assertTrue(gameEndDto.getEndResponseB().getIsWin());
        assertFalse(gameEndDto.getEndResponseA().getIsWin());

        assertEquals("A", gameEndDto.getEndResponseA().getLosingTeam());
        assertEquals("A", gameEndDto.getEndResponseB().getLosingTeam());

        assertEquals(memberId1, gameEndDto.getEndResponseA().getLosingLeaderId());
        assertEquals(memberId1, gameEndDto.getEndResponseB().getLosingLeaderId());

        assertEquals(memberId3, gameEndDto.getEndResponseA().getWinningLeaderId());
        assertEquals(memberId3, gameEndDto.getEndResponseB().getWinningLeaderId());

        List<PlayerWeaponDto> players = new ArrayList<>();
        players.add(PlayerWeaponDto.builder().memberId(memberId1).leftWeapon("SWORD").rightWeapon("SHIELD").build());
        players.add(PlayerWeaponDto.builder().memberId(memberId2).leftWeapon("TWIN").rightWeapon("HAND").build());
        players.add(PlayerWeaponDto.builder().memberId(memberId3).leftWeapon("SWORD").rightWeapon("SHIELD").build());
        players.add(PlayerWeaponDto.builder().memberId(memberId4).leftWeapon("TWIN").rightWeapon("HAND").build());

        assertEquals(players, gameEndDto.getEndResponseA().getPlayers());
        assertEquals(players, gameEndDto.getEndResponseB().getPlayers());

    }
}