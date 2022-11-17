package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.prepare.PlayerDataDto;
import com.a301.theknight.domain.game.dto.prepare.request.GameOrderRequest;
import com.a301.theknight.domain.game.dto.prepare.request.GameWeaponChoiceRequest;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GameWeaponResponse;
import com.a301.theknight.domain.game.entity.Game;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.member.entity.Member;
import com.a301.theknight.domain.player.entity.Player;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GamePrepareServiceTest {

    @Mock
    GameRepository gameRepository;
    @Mock
    GameRedisRepository gameRedisRepository;
    @Mock
    Game testGame;
    @Mock
    Member memberA;
    @Mock
    Member memberB;

    GamePrepareService gamePrepareService;
    Long testGameId;
    Long testMemberAId;
    Long testMemberBId;
    Player playerA;
    Player playerB;
    InGame testInGame;

    @Test
    @DisplayName("전체 플레이어 조회 / PlayerDataDto 타입 변환 테스트")
    void changeTypeToPlayerDto() throws Exception {
        //given
        InGamePlayer testPlayer = InGamePlayer.builder()
                .memberId(1L)
                .nickname("test player")
                .image("image")
                .leftCount(1)
                .rightCount(2)
                .team(Team.B)
                .leader(false).build();
        testPlayer.randomChoiceWeapon(Weapon.SWORD);
        testPlayer.randomChoiceWeapon(Weapon.HAND);
        testPlayer.saveOrder(3);

        //when
        PlayerDataDto playerDataDto = PlayerDataDto.toDto(testPlayer);
        //then
        assertThat(playerDataDto.getMemberId()).isEqualTo(testPlayer.getMemberId());
        assertThat(playerDataDto.getNickname()).isEqualTo(testPlayer.getNickname());
        assertThat(playerDataDto.getOrder()).isEqualTo(testPlayer.getOrder());
        assertThat(playerDataDto.getLeftCount()).isEqualTo(testPlayer.getLeftCount());
        assertThat(playerDataDto.getRightCount()).isEqualTo(testPlayer.getRightCount());
        assertThat(playerDataDto.getTeam()).isEqualTo(testPlayer.getTeam().name());
        assertThat(playerDataDto.getWeapons().get(0)).isEqualTo(testPlayer.getLeftWeapon().name());
        assertThat(playerDataDto.getWeapons().get(1)).isEqualTo(testPlayer.getRightWeapon().name());
    }

    @Test
    @DisplayName("Prepare / 리더 선택")
    void prepare() throws Exception {
        //given
        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(GameWeaponData.builder().build()));

        given(testGame.getPlayers()).willReturn(List.of(playerA, playerB));
        given(testGame.getTeamLeader(Team.A)).willReturn(Optional.of(playerA));
        given(testGame.getTeamLeader(Team.B)).willReturn(Optional.of(playerB));
        //when
//        gamePrepareService.prepare(testGameId);
        //then
        assertThat(playerA.isLeader()).isTrue();
        assertThat(playerB.isLeader()).isTrue();
    }

    @Test
    @DisplayName("Prepare / 응답 테스트")
    void prepareReturnTest() throws Exception {
        //given
        GameWeaponData gameWeaponData = makeWeaponData(SWORD, TWIN, SHIELD, HAND);
        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(gameWeaponData));

        given(testGame.getPlayers()).willReturn(List.of(playerA, playerB));
        given(testGame.getTeamLeader(Team.A)).willReturn(Optional.of(playerA));
        given(testGame.getTeamLeader(Team.B)).willReturn(Optional.of(playerB));
        //when
//        GamePrepareDto prepare = gamePrepareService.prepare(testGameId);
        //then
//        assertThat(prepare.getGameWeaponData().getSword()).isEqualTo(SWORD);
//        assertThat(prepare.getGameWeaponData().getTwin()).isEqualTo(TWIN);
//        assertThat(prepare.getGameWeaponData().getShield()).isEqualTo(SHIELD);
//        assertThat(prepare.getGameWeaponData().getHand()).isEqualTo(HAND);
//
//        assertThat(prepare.getGameLeaderDto().getTeamA().getMemberId()).isEqualTo(testMemberAId);
//        assertThat(prepare.getGameLeaderDto().getTeamB().getMemberId()).isEqualTo(testMemberBId);
    }

    @Test
    @DisplayName("무기 선택 / 무기가 부족한 경우")
    void notEnoughWeapon() throws Exception {
        //given
        GameWeaponData gameWeaponData = makeWeaponData(0, TWIN, SHIELD, HAND);
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .team(Team.A).build();
        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(gameWeaponData));
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));
        //when
        GameWeaponChoiceRequest request = new GameWeaponChoiceRequest();
        request.setWeapon(Weapon.SWORD);
        //then
        CustomWebSocketException exception = assertThrows(CustomWebSocketException.class,
                () -> gamePrepareService.choiceWeapon(testGameId, testMemberAId, request));
        assertThat(exception.getErrorCode()).isEqualTo(NOT_ENOUGH_WEAPON);
    }

    @Test
    @DisplayName("무기 선택 / 이미 2개를 선택한 경우")
    void alreadyAllChoice() throws Exception {
        //given
        GameWeaponData gameWeaponData = makeWeaponData(SWORD, TWIN, SHIELD, SWORD);
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .team(Team.A).build();
        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(gameWeaponData));
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));
        //when
        inGamePlayer.choiceWeapon(Weapon.SWORD, gameWeaponData);
        inGamePlayer.choiceWeapon(Weapon.SHIELD, gameWeaponData);

        GameWeaponChoiceRequest request = new GameWeaponChoiceRequest();
        request.setWeapon(Weapon.TWIN);
        //then
        CustomWebSocketException exception = assertThrows(CustomWebSocketException.class,
                () -> gamePrepareService.choiceWeapon(testGameId, testMemberAId, request));
        assertThat(exception.getErrorCode()).isEqualTo(SELECT_WEAPON_IS_FULL);
    }

    @Test
    @DisplayName("무기 선택 / 상태 값 확인")
    void checkInGamePlayerField() throws Exception {
        //given
        GameWeaponData gameWeaponData = makeWeaponData(SWORD, TWIN, SHIELD, SWORD);
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .team(Team.A).build();

        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(gameWeaponData));
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));
        //when
        GameWeaponChoiceRequest request = new GameWeaponChoiceRequest();
        request.setWeapon(Weapon.SWORD);
        gamePrepareService.choiceWeapon(testGameId, testMemberAId, request);

        request.setWeapon(Weapon.TWIN);
        gamePrepareService.choiceWeapon(testGameId, testMemberAId, request);
        //then
        assertThat(inGamePlayer.getLeftWeapon()).isEqualTo(Weapon.SWORD);
        assertThat(inGamePlayer.getRightWeapon()).isEqualTo(Weapon.TWIN);
        assertThat(gameWeaponData.getSword()).isEqualTo(SWORD - 1);
        assertThat(gameWeaponData.getTwin()).isEqualTo(TWIN - 1);
    }

    @Test
    @DisplayName("무기 선택 / 응답 테스트")
    void checkResponse() throws Exception {
        //given
        GameWeaponData gameWeaponData = makeWeaponData(SWORD, TWIN, SHIELD, SWORD);
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .team(Team.A).build();

        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(gameWeaponData));
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));
        //when
        GameWeaponChoiceRequest request = new GameWeaponChoiceRequest();
        request.setWeapon(Weapon.SWORD);

        GameWeaponResponse response = gamePrepareService.choiceWeapon(testGameId, testMemberAId, request);
        //then
        assertThat(response.getTeam()).isEqualTo(Team.A);
        assertThat(response.getGameWeaponData()).isEqualTo(gameWeaponData);
        assertThat(response.getGameWeaponData().getSword()).isEqualTo(SWORD - 1);
    }


    @Test
    @DisplayName("무기 취소 / 상태 값 테스트")
    void cancelWeapon() throws Exception {
        //given
        GameWeaponData gameWeaponData = makeWeaponData(SWORD - 1, TWIN, SHIELD - 1, HAND);
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .memberId(testMemberAId)
                .team(Team.A).build();

        inGamePlayer.randomChoiceWeapon(Weapon.SWORD);
        inGamePlayer.randomChoiceWeapon(Weapon.SHIELD);

        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(gameWeaponData));
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));
        //when
        gamePrepareService.cancelWeapon(testGameId, testMemberAId, Hand.LEFT);
        //then
        assertThat(inGamePlayer.getRightWeapon()).isNull();
        assertThat(inGamePlayer.getLeftWeapon()).isEqualTo(Weapon.SHIELD);
        assertThat(gameWeaponData.getSword()).isEqualTo(SWORD);
    }

    @Test
    @DisplayName("무기 취소 / 응답 값 테스트")
    void cancelWeaponResponseTest() throws Exception {
        //given
        GameWeaponData gameWeaponData = makeWeaponData(SWORD - 1, TWIN, SHIELD - 1, HAND);
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .memberId(testMemberAId)
                .team(Team.A).build();
        inGamePlayer.randomChoiceWeapon(Weapon.SWORD);
        inGamePlayer.randomChoiceWeapon(Weapon.SHIELD);

        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(gameWeaponData));
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));
        //when
        GameWeaponResponse response = gamePrepareService.cancelWeapon(testGameId, testMemberAId, Hand.LEFT);
        //then
        assertThat(response.getTeam()).isEqualTo(Team.A);
        assertThat(response.getGameWeaponData()).isEqualTo(gameWeaponData);
        assertThat(gameWeaponData.getSword()).isEqualTo(SWORD);
    }

    @Test
    @DisplayName("순서 선택 / 범위 외의 요청 순서 번호")
    void orderValueTest() throws Exception {
        GameOrderRequest request = new GameOrderRequest();

        request.setOrderNumber((CAPACITY / 2) + 1);
        CustomWebSocketException exception = assertThrows(CustomWebSocketException.class, () ->
                gamePrepareService.saveOrder(testGameId, testMemberAId, Team.A, request));
        assertThat(exception.getErrorCode()).isEqualTo(ORDER_NUMBER_IS_INVALID);

        request.setOrderNumber(-1);
        CustomWebSocketException exception2 = assertThrows(CustomWebSocketException.class, () ->
                gamePrepareService.saveOrder(testGameId, testMemberAId, Team.A, request));
        assertThat(exception2.getErrorCode()).isEqualTo(ORDER_NUMBER_IS_INVALID);
    }

    @Test
    @DisplayName("순서 선택 / 다른 팀 순서 요청한 경우")
    void orderTeamTest() throws Exception {
        //given
        GameOrderRequest request = new GameOrderRequest();
        request.setOrderNumber(CAPACITY / 2 - 1);
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .memberId(testMemberAId)
                .team(Team.A).build();

        inGamePlayer.randomChoiceWeapon(Weapon.SWORD);
        inGamePlayer.randomChoiceWeapon(Weapon.SHIELD);

        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));
        //when
        //then
        CustomWebSocketException exception = assertThrows(CustomWebSocketException.class, () ->
                gamePrepareService.saveOrder(testGameId, testMemberAId, Team.B, request));
        assertThat(exception.getErrorCode()).isEqualTo(NOT_MATCH_REQUEST_TEAM);
    }

    @Test
    @DisplayName("순서 선택 / 이미 선택 된 번호를 선택하는 경우")
    void alreadySelectedNumber() throws Exception {
        //given
        GameOrderRequest request = new GameOrderRequest();
        request.setOrderNumber(2);
        InGamePlayer inGamePlayer1 = InGamePlayer.builder()
                .memberId(testMemberAId).team(Team.A).build();
        InGamePlayer inGamePlayer2 = InGamePlayer.builder()
                .memberId(testMemberBId).team(Team.A).build();
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer1));
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberBId))
                .willReturn(Optional.of(inGamePlayer2));
        //when
        gamePrepareService.saveOrder(testGameId, testMemberAId, Team.A, request);
        //then
        CustomWebSocketException exception = assertThrows(CustomWebSocketException.class, () ->
                gamePrepareService.saveOrder(testGameId, testMemberBId, Team.A, request));
        assertThat(exception.getErrorCode()).isEqualTo(ALREADY_SELECTED_ORDER_NUMBER);
    }

    @Test
    @DisplayName("순서 선택 / 순서 선택, 수정 테스트")
    void dataChangeTest() throws Exception {
        //given
        InGamePlayer inGamePlayer1 = InGamePlayer.builder()
                .memberId(testMemberAId).team(Team.A).build();
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer1));
        //when
        GameOrderRequest request = new GameOrderRequest();
        request.setOrderNumber(1);
        gamePrepareService.saveOrder(testGameId, testMemberAId, Team.A, request);
        request.setOrderNumber(2);
        gamePrepareService.saveOrder(testGameId, testMemberAId, Team.A, request);
        //then
        assertThat(testInGame.getTeamAInfo().getOrderList()[0]).isNull();
        assertThat(testInGame.getTeamAInfo().getOrderList()[1]).isNotNull();
        assertThat(testInGame.getTeamAInfo().getOrderList()[1].getMemberId())
                .isEqualTo(testMemberAId);
    }

    @Test
    @DisplayName("순서 선택 / 응답 확인")
    void orderResponseTest() throws Exception {
        //given
        InGamePlayer inGamePlayer1 = InGamePlayer.builder()
                .memberId(testMemberAId).team(Team.A).build();
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer1));
        //when
        GameOrderRequest request = new GameOrderRequest();
        request.setOrderNumber(1);

        GameOrderResponse response = gamePrepareService.saveOrder(testGameId, testMemberAId, Team.A, request);
        GameOrderDto[] orderList = response.getOrderList();
        //then
        assertThat(orderList.length).isEqualTo(testGame.getCapacity() / 2);
        assertThat(orderList[0]).isNotNull();
        assertThat(orderList[0].getMemberId()).isEqualTo(testMemberAId);
        for (int i = 1; i < CAPACITY / 2; i++) {
            assertThat(orderList[i]).isNull();
        }
    }

    @Test
    @DisplayName("선택 완료 / 리더 요청 확인")
    void isNotLeader(@Mock Member member1) throws Exception {
        //given
        Long member1Id = 3L;
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .memberId(member1Id).team(Team.A).build();
        given(gameRedisRepository.getInGamePlayer(testGameId, member1Id))
                .willReturn(Optional.of(inGamePlayer));
        //when
        CustomWebSocketException exception = assertThrows(CustomWebSocketException.class, () ->
                gamePrepareService.completeSelect(testGameId, member1Id));
        //then
        assertThat(exception.getErrorCode()).isEqualTo(CAN_COMPLETE_BY_LEADER);
    }

    @Test
    @DisplayName("선택 완료 / 한 명이 여러 순서 선택 or 순서 선택 안되어 있는 경우")
    void wrongOrder(@Mock Member member1, @Mock Member member2) throws Exception {
        //given
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .memberId(testMemberAId).team(Team.A).leader(true).build();
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));

        GameOrderDto[] orderList = testInGame.getTeamAInfo().getOrderList();
        addPlayer(member1, member2);
        orderList[0] = orderList[1];

        //when
        CustomWebSocketException exception = assertThrows(CustomWebSocketException.class, () ->
                gamePrepareService.completeSelect(testGameId, testMemberAId));
        orderList[0] = null;
        CustomWebSocketException exception2 = assertThrows(CustomWebSocketException.class, () ->
                gamePrepareService.completeSelect(testGameId, testMemberAId));
        //then
        assertThat(exception.getErrorCode()).isEqualTo(CAN_NOT_COMPLETE_ORDER_SELECT);
        assertThat(exception2.getErrorCode()).isEqualTo(CAN_NOT_COMPLETE_ORDER_SELECT);
    }

    @Test
    @DisplayName("선택 완료 / 모든 무기 선택 X")
    void notAllSelectWeapon(@Mock Member member1, @Mock Member member2) throws Exception {
        //given
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .memberId(testMemberAId).team(Team.A).leader(true).build();
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));
        addPlayer(member1, member2);

        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(GameWeaponData.builder().sword(1).build()));

        //when
        CustomWebSocketException exception = assertThrows(CustomWebSocketException.class, () ->
                gamePrepareService.completeSelect(testGameId, testMemberAId));
        //then
        assertThat(exception.getErrorCode()).isEqualTo(CAN_NOT_COMPLETE_WEAPON_SELECT);
    }

    @Test
    @DisplayName("선택 완료 / 플레이어 선택 무기와 게임 무기 개수가 다른 경우")
    void notValidWeaponCount(@Mock Member member1, @Mock Member member2) throws Exception {
        //given
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .memberId(testMemberAId).team(Team.A).leader(true).build();
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));

        List<Player> players = addPlayer(member1, member2);

        List<InGamePlayer> inGamePlayerList = new ArrayList<>();
        players.forEach(player -> {
            InGamePlayer anotherInGamePlayer = InGamePlayer.builder()
                    .memberId(player.getMember().getId())
                    .team(Team.A).build();

            anotherInGamePlayer.randomChoiceWeapon(Weapon.SWORD);
            anotherInGamePlayer.randomChoiceWeapon(Weapon.SHIELD);
            inGamePlayerList.add(anotherInGamePlayer);
        });

        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(GameWeaponData.builder().build()));
        given(gameRedisRepository.getTeamPlayerList(testGameId, Team.A))
                .willReturn(inGamePlayerList);
        //when
        CustomWebSocketException exception = assertThrows(CustomWebSocketException.class, () ->
                gamePrepareService.completeSelect(testGameId, testMemberAId));
        //then
        assertThat(exception.getErrorCode()).isEqualTo(CAN_NOT_COMPLETE_WEAPON_SELECT);
    }

    @Test
    @DisplayName("선택 완료 / completeSelect Flag 확인")
    void completeSelectTest(@Mock Member member1, @Mock Member member2) throws Exception {
        //given
        InGamePlayer inGamePlayer = InGamePlayer.builder()
                .memberId(testMemberAId).team(Team.A).leader(true).build();
        given(gameRedisRepository.getInGamePlayer(testGameId, testMemberAId))
                .willReturn(Optional.of(inGamePlayer));

        List<Player> players = addPlayer(member1, member2);

        List<InGamePlayer> inGamePlayerList = fillInGamePlayers(players);

        given(gameRedisRepository.getGameWeaponData(testGameId, Team.A))
                .willReturn(Optional.of(GameWeaponData.builder().build()));
        given(gameRedisRepository.getTeamPlayerList(testGameId, Team.A))
                .willReturn(inGamePlayerList);
        //when
        gamePrepareService.completeSelect(testGameId, testMemberAId);
        //then
        assertThat(testInGame.getTeamAInfo().isSelected()).isTrue();
    }

    private List<InGamePlayer> fillInGamePlayers(List<Player> players) {
        List<InGamePlayer> inGamePlayerList = new ArrayList<>();
        List<Weapon> weaponList = new ArrayList<>();

        for (int i = 0; i < SWORD; i++) {
            weaponList.add(Weapon.SWORD);
        }
        for (int i = 0; i < SHIELD; i++) {
            weaponList.add(Weapon.SHIELD);
        }
        for (int i = 0; i < TWIN; i++) {
            weaponList.add(Weapon.TWIN);
        }
        for (int i = 0; i < HAND; i++) {
            weaponList.add(Weapon.HAND);
        }
        for (int i = 0; i + 1 < CAPACITY; i += 2) {
            InGamePlayer inGamePlayer = InGamePlayer.builder()
                            .memberId(players.get(1).getMember().getId())
                            .team(Team.A).build();
            inGamePlayer.randomChoiceWeapon(weaponList.get(i));
            inGamePlayer.randomChoiceWeapon(weaponList.get(i + 1));

            inGamePlayerList.add(inGamePlayer);
        }

        return inGamePlayerList;
    }

    private final static String TITLE = "game title";
    private final static int CAPACITY = 6;
    private final static int SWORD = 2;
    private final static int TWIN = 1;
    private final static int SHIELD = 2;
    private final static int HAND = 1;

    @BeforeEach
    void setup() {
        testGameId = 1L;
        stubbingTestGame();

        testMemberAId = 1L;
        testMemberBId = 2L;
        stubbingMember(testMemberAId, memberA);
        stubbingMember(testMemberBId, memberB);

        playerA = makePlayer(memberA, testGame);
        playerA.setOwner();
        playerA.selectTeam(Team.A);

        playerB = makePlayer(memberB, testGame);
        playerB.selectTeam(Team.B);

        gamePrepareService = new GamePrepareService(gameRepository, gameRedisRepository);
        given(gameRepository.findById(testGameId)).willReturn(Optional.of(testGame));
        testInGame = makeInGame(GameStatus.PREPARE, Team.A, testGame);
        given(gameRedisRepository.getInGame(testGameId)).willReturn(Optional.of(testInGame));
    }


    private List<Player> addPlayer(Member member1, Member member2) {
        Long member1Id = 3L;
        Long member2Id = 4L;
        stubbingMember(member1Id, member1);
        stubbingMember(member2Id, member2);

        Player player1 = makePlayer(member1, testGame);
        player1.selectTeam(Team.A);
        Player player2 = makePlayer(member2, testGame);
        player2.selectTeam(Team.A);

        given(testGame.getPlayers()).willReturn(List.of(playerA, player1, player2));

        List<Player> playerList = List.of(this.playerA, player1, player2);
        GameOrderDto[] orderList = testInGame.getTeamAInfo().getOrderList();
        choiceOrder(playerList, orderList);

        return playerList;
    }

    private void choiceOrder(List<Player> playerList, GameOrderDto[] orderList) {
        for (int i = 1; i <= CAPACITY / 2; i++) {
            Player player = playerList.get(i - 1);
            orderList[i - 1] = GameOrderDto.builder()
                    .memberId(player.getMember().getId()).build();
        }
    }

    private GameWeaponData makeWeaponData(int sword, int twin, int shield, int hand) {
        return GameWeaponData.builder()
                .sword(sword)
                .twin(twin)
                .shield(shield)
                .hand(hand).build();
    }

    private InGame makeInGame(GameStatus gameStatus, Team team, Game game) {
        return InGame.builder()
                .gameStatus(gameStatus)
                .currentAttackTeam(team)
                .maxMemberNum(game.getCapacity())
                .teamAInfo(makeTeamInfoData(game.getCapacity(), testMemberAId))
                .teamBInfo(makeTeamInfoData(game.getCapacity(), testMemberBId))
                .turnData(new TurnData()).build();
    }

    private Player makePlayer(Member member, Game game) {
        return Player.builder()
                .member(member)
                .game(game).build();
    }

    private void stubbingMember(Long memberId, Member member) {
        given(member.getId()).willReturn(memberId);
        given(member.getNickname()).willReturn("member");
    }

    private void stubbingTestGame() {
        given(testGame.getId()).willReturn(testGameId);
        given(testGame.getCapacity()).willReturn(CAPACITY);
        given(testGame.getSword()).willReturn(SWORD);
        given(testGame.getShield()).willReturn(SHIELD);
        given(testGame.getTwin()).willReturn(TWIN);
        given(testGame.getHand()).willReturn(HAND);
        given(testGame.isCanStart()).willReturn(true);
    }

    private static TeamInfoData makeTeamInfoData(int capacity, long leaderId) {
        int peopleNum = capacity / 2;

        return TeamInfoData.builder()
                .currentAttackIndex(peopleNum - 1)
                .orderList(new GameOrderDto[peopleNum])
                .leaderId(leaderId).build();
    }

}