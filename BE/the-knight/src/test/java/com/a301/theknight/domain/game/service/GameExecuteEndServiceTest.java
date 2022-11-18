package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.attack.DefendPlayerIdDto;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.end.response.GameEndResponse;
import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameResult;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.Hand;
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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class GameExecuteEndServiceTest {

     Member testMember1, testMember2, testMember3, testMember4;
     Ranking testRanking1, testRanking2, testRanking3, testRanking4;
     Game testGame;
     Player testPlayer1, testPlayer2, testPlayer3, testPlayer4;
     InGame testInGame;
     InGamePlayer testInGamePlayer1, testInGamePlayer2, testInGamePlayer3, testInGamePlayer4;
     long memberId1 = 1L, memberId2 = 2L, memberId3 = 3L, memberId4 = 4L, gameId = 5L;
//    @InjectMocks
//    GameExecuteEndService gameExecuteEndService;
    @Mock
    GameRedisRepository gameRedisRepository;
    @Mock
    GameRepository gameRepository;
    @Mock
    RankingRepository rankingRepository;
    @Mock
    PlayerRepository playerRepository;

    @BeforeEach
     void beforeEach() {
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

        testRanking1.saveWinScore();    // 15
        testRanking1.saveWinScore();
        testRanking1.saveLoseScore();
//        testRanking2                  // 0
        testRanking3.saveWinScore();    // 20
        testRanking3.saveWinScore();
        testRanking4.saveWinScore();    // 5
        testRanking4.saveLoseScore();

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
                .build();
        testInGame.initTurnData();

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
                .nickname(testMember1.getNickname())
                .team(testPlayer1.getTeam())
                .leftCount(0)
                .rightCount(0)
                .leader(true).build();
        testInGamePlayer1.randomChoiceWeapon(Weapon.SWORD);
        testInGamePlayer1.randomChoiceWeapon(Weapon.SHIELD);
        testInGamePlayer1.saveOrder(0);

        testInGamePlayer2 = InGamePlayer.builder()
                .memberId(memberId2)
                .nickname(testMember2.getNickname())
                .team(testPlayer2.getTeam())
                .leftCount(0)
                .rightCount(0)
                .leader(false).build();
        testInGamePlayer2.randomChoiceWeapon(Weapon.TWIN);
        testInGamePlayer2.randomChoiceWeapon(Weapon.HAND);
        testInGamePlayer2.saveOrder(1);

        testInGamePlayer3 = InGamePlayer.builder()
                .memberId(memberId3)
                .nickname(testMember3.getNickname())
                .team(testPlayer3.getTeam())
                .leftCount(0)
                .rightCount(0)
                .leader(true).build();
        testInGamePlayer3.randomChoiceWeapon(Weapon.SWORD);
        testInGamePlayer3.randomChoiceWeapon(Weapon.SHIELD);
        testInGamePlayer3.saveOrder(0);

        testInGamePlayer4 = InGamePlayer.builder()
                .memberId(memberId4)
                .nickname(testMember4.getNickname())
                .team(testPlayer4.getTeam())
                .leftCount(0)
                .rightCount(0)
                .leader(false).build();
        testInGamePlayer4.randomChoiceWeapon(Weapon.TWIN);
        testInGamePlayer4.randomChoiceWeapon(Weapon.HAND);
        testInGamePlayer4.saveOrder(1);

    }

    @Test
    @DisplayName("턴 수행 테스트 - kill 발생 - isDefendPass가 참")
    void defendPass() throws Exception {
        //given
        stubbingInGame();
        given(gameRedisRepository.getInGamePlayer(gameId, memberId4)).willReturn(Optional.of(testInGamePlayer4));

        GameAttackRequest gameAttackRequest = new GameAttackRequest();
//        gameAttackRequest.setAttacker(new AttackPlayerDto(memberId1));
        gameAttackRequest.setDefender(new DefendPlayerIdDto(memberId4));
        gameAttackRequest.setWeapon(Weapon.TWIN);
        gameAttackRequest.setHand(Hand.LEFT);

        GameDefenseRequest gameDefenseRequest = new GameDefenseRequest();
//        gameDefenseRequest.setDefender(new DefendPlayerDto(memberId4));
        gameDefenseRequest.setHand(Hand.LEFT);


        testInGame.getTurnData().recordAttackData(testInGamePlayer1, testInGamePlayer4, gameAttackRequest);
        testInGame.getTurnData().recordDefenseData(testInGamePlayer4, gameDefenseRequest);
        testInGame.getTurnData().getDefenseData().defendPass();

        //when
//        gameExecuteEndService.executeTurn(gameId);

        //then
        assertTrue(testInGamePlayer4.isDead());
        assertEquals(GameStatus.ATTACK, testInGame.getGameStatus());
    }

    @Test
    @DisplayName("턴 수행 테스트 - kill 발생 - resultCount가 0보다 작은 경우")
    void AttackMore() throws Exception {
        //given
        stubbingInGame();
        given(gameRedisRepository.getInGamePlayer(gameId, memberId4)).willReturn(Optional.of(testInGamePlayer4));
        testInGamePlayer4.changeCount(1, Hand.LEFT);

        GameAttackRequest gameAttackRequest = new GameAttackRequest();
//        gameAttackRequest.setAttacker(new AttackPlayerDto(memberId1));
        gameAttackRequest.setDefender(new DefendPlayerIdDto(memberId4));
        gameAttackRequest.setWeapon(Weapon.TWIN);
        gameAttackRequest.setHand(Hand.LEFT);

        GameDefenseRequest gameDefenseRequest = new GameDefenseRequest();
//        gameDefenseRequest.setDefender(new DefendPlayerDto(memberId4));
        gameDefenseRequest.setHand(Hand.LEFT);


        testInGame.getTurnData().recordAttackData(testInGamePlayer1, testInGamePlayer4, gameAttackRequest);
        testInGame.getTurnData().recordDefenseData(testInGamePlayer4, gameDefenseRequest);

        //when
//        gameExecuteEndService.executeTurn(gameId);

        //then
        assertTrue(testInGamePlayer4.isDead());
        assertEquals(GameStatus.ATTACK, testInGame.getGameStatus());
    }

    @Test
    @DisplayName("턴 수행 테스트 - kill 발생 - 방어자가 리더인 경우")
    void LeaderDie() throws Exception {
        //given
        stubbingInGame();
        given(gameRedisRepository.getInGamePlayer(gameId, memberId3)).willReturn(Optional.of(testInGamePlayer3));

        GameAttackRequest gameAttackRequest = new GameAttackRequest();
//        gameAttackRequest.setAttacker(new AttackPlayerDto(memberId1));
        gameAttackRequest.setDefender(new DefendPlayerIdDto(memberId3));
        gameAttackRequest.setWeapon(Weapon.TWIN);
        gameAttackRequest.setHand(Hand.LEFT);

        GameDefenseRequest gameDefenseRequest = new GameDefenseRequest();
//        gameDefenseRequest.setDefender(new DefendPlayerDto(memberId3));
        gameDefenseRequest.setHand(Hand.LEFT);


        testInGame.getTurnData().recordAttackData(testInGamePlayer1, testInGamePlayer3, gameAttackRequest);
        testInGame.getTurnData().recordDefenseData(testInGamePlayer3, gameDefenseRequest);
        testInGame.getTurnData().getDefenseData().defendPass();

        //when
//        gameExecuteEndService.executeTurn(gameId);

        //then
        assertTrue(testInGamePlayer3.isDead());
        assertEquals(GameStatus.END, testInGame.getGameStatus());
    }

    @Test
    @DisplayName("턴 수행 테스트 - kill 발생 X - GameExecuteResponse 확인")
    void responseCheck() throws Exception {
        //given
        stubbingInGame();
        given(gameRedisRepository.getInGamePlayer(gameId, memberId4)).willReturn(Optional.of(testInGamePlayer4));
        testInGamePlayer4.changeCount(3, Hand.LEFT);

        GameAttackRequest gameAttackRequest = new GameAttackRequest();
//        gameAttackRequest.setAttacker(new AttackPlayerDto(memberId1));
        gameAttackRequest.setDefender(new DefendPlayerIdDto(memberId4));
        gameAttackRequest.setWeapon(Weapon.SWORD);
        gameAttackRequest.setHand(Hand.LEFT);

        GameDefenseRequest gameDefenseRequest = new GameDefenseRequest();
//        gameDefenseRequest.setDefender(new DefendPlayerDto(memberId4));
        gameDefenseRequest.setHand(Hand.LEFT);


        testInGame.getTurnData().recordAttackData(testInGamePlayer1, testInGamePlayer4, gameAttackRequest);
        testInGame.getTurnData().recordDefenseData(testInGamePlayer4, gameDefenseRequest);

        //when
//        GameExecuteResponse gameExecuteResponse = gameExecuteEndService.executeTurn(gameId);

        //then
        assertFalse(testInGamePlayer4.isDead());
        assertEquals(GameStatus.ATTACK, testInGame.getGameStatus());
//        assertEquals(AttackerDto.builder().memberId(memberId1).weapon(Weapon.SWORD.name()).hand(Hand.LEFT.name()).build(), gameExecuteResponse.getAttacker());
//        assertEquals(DefenderDto.builder().memberId(memberId4).hand(Hand.LEFT.name()).isDead(false).restCount(2).passedDefense(false).build(), gameExecuteResponse.getDefender());
//        assertEquals("A", gameExecuteResponse.getAttackTeam());
    }

    @Test
    @Order(1)
    @DisplayName("게임 종료 테스트")
    void gameEnd() throws Exception {
        // 게임 관련된 데이터 만들기
        // given
        stubbingData();
        testInGamePlayer1.death();

        // 게임 종료 실행
        // when
//        GameEndResponse gameEndResponse = gameExecuteEndService.gameEnd(gameId);
        GameEndResponse gameEndResponse = null;

//        1. Game의 status가 END로 변경되는지 확인
//        2. ranking 사용자들 점수가 정확한 값인지를 확인
//        3. Player의 게임 결과가 이긴 팀은 WIN, 진 팀은 LOSE로 바뀌었는지 확인
//        4. gameEndResponse 결과 확인
        // then

//        1. Game의 status가 END로 변경되는지 확인
        assertEquals(GameStatus.END, testGame.getStatus());

//        2. ranking 사용자들 점수가 정확한 값인지를 확인
        assertEquals(10, rankingRepository.findByMemberId(memberId1).get().getScore());
        assertEquals(0, rankingRepository.findByMemberId(memberId2).get().getScore());
        assertEquals(30, rankingRepository.findByMemberId(memberId3).get().getScore());
        assertEquals(15, rankingRepository.findByMemberId(memberId4).get().getScore());

//        3. Player의 게임 결과가 이긴 팀은 WIN, 진 팀은 LOSE로 변경되는지 확인
        assertEquals(GameResult.LOSE, playerRepository.findByGameIdAndMemberId(gameId, memberId1).get().getResult());
        assertEquals(GameResult.LOSE, playerRepository.findByGameIdAndMemberId(gameId, memberId2).get().getResult());
        assertEquals(GameResult.WIN, playerRepository.findByGameIdAndMemberId(gameId, memberId3).get().getResult());
        assertEquals(GameResult.WIN, playerRepository.findByGameIdAndMemberId(gameId, memberId4).get().getResult());

//        4. gameEndResponse 결과 확인
        assertEquals("B", gameEndResponse.getWinningTeam());

        assertEquals(memberId1, gameEndResponse.getTeamALeaderId());
        assertEquals(memberId3, gameEndResponse.getTeamBLeaderId());

        List<PlayerDataDto> players = new ArrayList<>();
        players.add(PlayerDataDto.toDto(testInGamePlayer1));
        players.add(PlayerDataDto.toDto(testInGamePlayer2));
        players.add(PlayerDataDto.toDto(testInGamePlayer3));
        players.add(PlayerDataDto.toDto(testInGamePlayer4));

        assertEquals(players, gameEndResponse.getPlayers());

    }

    private void stubbingData() {
        stubbingInGame();
        stubbingInGamePlayer();
        stubbingGame();
        stubbingPlayer();
        stubbingRanking();
    }

    private void stubbingInGame() {
        given(gameRedisRepository.getInGame(gameId)).willReturn(Optional.of(testInGame));

    }

    private void stubbingInGamePlayer() {
        given(gameRedisRepository.getInGamePlayer(gameId, memberId1)).willReturn(Optional.of(testInGamePlayer1));
//        given(gameRedisRepository.getInGamePlayer(gameId, memberId2)).willReturn(Optional.of(testInGamePlayer2));
//        given(gameRedisRepository.getInGamePlayer(gameId, memberId3)).willReturn(Optional.of(testInGamePlayer3));
//        given(gameRedisRepository.getInGamePlayer(gameId, memberId4)).willReturn(Optional.of(testInGamePlayer4));

        List<InGamePlayer> playerList = new ArrayList<>();
        playerList.add(testInGamePlayer1);
        playerList.add(testInGamePlayer2);
        playerList.add(testInGamePlayer3);
        playerList.add(testInGamePlayer4);
        given(gameRedisRepository.getInGamePlayerList(gameId)).willReturn(playerList);
    }

    private void stubbingGame() {

        given(gameRepository.findById(gameId)).willReturn(Optional.of(testGame));
    }

    private void stubbingPlayer() {
        given(playerRepository.findByGameIdAndMemberId(gameId, memberId1)).willReturn(Optional.of(testPlayer1));
        given(playerRepository.findByGameIdAndMemberId(gameId, memberId2)).willReturn(Optional.of(testPlayer2));
        given(playerRepository.findByGameIdAndMemberId(gameId, memberId3)).willReturn(Optional.of(testPlayer3));
        given(playerRepository.findByGameIdAndMemberId(gameId, memberId4)).willReturn(Optional.of(testPlayer4));
    }

    private void stubbingRanking() {
        given(rankingRepository.findByMemberId(memberId1)).willReturn(Optional.of(testRanking1));
        given(rankingRepository.findByMemberId(memberId2)).willReturn(Optional.of(testRanking2));
        given(rankingRepository.findByMemberId(memberId3)).willReturn(Optional.of(testRanking3));
        given(rankingRepository.findByMemberId(memberId4)).willReturn(Optional.of(testRanking4));

    }
}