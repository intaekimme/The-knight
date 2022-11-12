package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.attack.AttackPlayerDto;
import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackPassRequest;
import com.a301.theknight.domain.game.dto.attack.request.GameAttackRequest;
import com.a301.theknight.domain.game.dto.attack.response.AttackResponse;
import com.a301.theknight.domain.game.dto.attacker.AttackerDto;
import com.a301.theknight.domain.game.dto.defense.request.GameDefensePassRequest;
import com.a301.theknight.domain.game.dto.defense.request.GameDefenseRequest;
import com.a301.theknight.domain.game.dto.defense.response.DefenseResponse;
import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameAttackDefenseServiceTest {

    @Mock
    GameRedisRepository gameRedisRepository;

    @Mock
    GameAttackDefenseService gameAttackDefenseService;

    private InGame inGame;
    private InGamePlayer attacker;
    private InGamePlayer defender;

    private List<InGamePlayer> TeamA;

    @BeforeEach
    void setup() {
        //  새로 공격, 방어 요청할 플레이어
        attacker = InGamePlayer.builder()
                .memberId(1L)
                .team(Team.A)
                .leftWeapon(Weapon.TWIN)
                .rightWeapon(Weapon.SWORD)
                .build();
        defender = InGamePlayer.builder()
                .memberId(2L)
                .team(Team.B)
                .leftWeapon(Weapon.SHIELD)
                .rightWeapon(Weapon.HAND)
                .build();

        //  기존 공격, 방어자 정보
        TurnData turnData = new TurnData();
        turnData.setAttackerId(4L);
        turnData.setDefenderId(1L);

        GameOrderDto[] orderList = new GameOrderDto[3];
        TeamA = new ArrayList<>();

        TeamA.add(attacker);
        orderList[0] = GameOrderDto.builder().memberId(attacker.getMemberId()).build();

        for(int i=1; i < 3; i++){
            long memberId = (i * 2) + 1;
            orderList[i] = GameOrderDto.builder().memberId(memberId).build();
            if(i==1){
                TeamA.add(InGamePlayer.builder()
                        .memberId(memberId)
                        .team(Team.A)
                        .leftWeapon(Weapon.TWIN)
                        .rightWeapon(Weapon.SWORD)
                        .isDead(true)
                        .build());
            }else{
                TeamA.add(InGamePlayer.builder()
                        .memberId(memberId)
                        .team(Team.A)
                        .leftWeapon(Weapon.TWIN)
                        .rightWeapon(Weapon.SWORD)
                        .build());
            }
        }

        TeamInfoData teamAInfoData = TeamInfoData.builder()
                .currentAttackIndex(3)
                .orderList(orderList)
                .leaderId(5L)
                .selected(false)
                .build();

        inGame = InGame.builder()
                .turnData(turnData)
                .gameStatus(GameStatus.ATTACK)
                .currentAttackTeam(Team.A)
                .maxMemberNum(4)
                .teamAInfo(teamAInfoData)
                .build();

        inGame.updateCurrentAttackTeam();

        gameAttackDefenseService = new GameAttackDefenseService(gameRedisRepository);
        lenient().when(gameRedisRepository.getInGame(1L)).thenReturn(Optional.of(inGame));
//        given(gameRedisRepository.getInGame(1L)).willReturn(Optional.of(inGame));
    }


    @DisplayName("공격자 조회 / A팀 조회")
    @Test
    void getAttacker() {
        //  given

        for(int i=0; i<3; i++){
            lenient().when(gameRedisRepository.getInGamePlayer(1L, (i*2) + 1)).thenReturn(Optional.of(TeamA.get(i)));
        }

        //  when
        AttackerDto attackerDto = gameAttackDefenseService.getAttacker(1L);

        //  then
        assertEquals(1L, attackerDto.getAttackerResponseA().getMemberId());
        assertEquals(1L, attackerDto.getAttackerResponseB().getMemberId());
        assertFalse(attackerDto.getAttackerResponseA().isOpposite());
        assertTrue(attackerDto.getAttackerResponseB().isOpposite());

    }

    @DisplayName("선공 조회")
    @Test
    void getPreAttack() {
        //  given
        //  when
        gameAttackDefenseService.getPreAttack(1L);
        //  then
        assertEquals(GameStatus.ATTACK, inGame.getGameStatus());
    }

    @DisplayName("공격 / 손에 들고 있는 무기로 공격")
    @Test
    void attack() {
        //  given
        GameAttackRequest attackRequest = new GameAttackRequest();
        attackRequest.setAttacker(new AttackPlayerDto(1L));
        attackRequest.setDefender(new DefendPlayerDto(2L));
        attackRequest.setWeapon(Weapon.TWIN);
        attackRequest.setHand(Hand.LEFT);
        given(gameRedisRepository.getInGamePlayer(1L, 1L)).willReturn(Optional.of(attacker));
        given(gameRedisRepository.getInGamePlayer(1L, 2L)).willReturn(Optional.of(defender));

        //when
        gameAttackDefenseService.attack(1L, 1L, attackRequest);

        //then
        assertEquals(1L, inGame.getTurnData().getAttackerId());
        assertEquals(2L, inGame.getTurnData().getDefenderId());
        assertFalse(inGame.getTurnData().isLyingAttack());
        assertEquals(GameStatus.ATTACK_DOUBT, inGame.getGameStatus());

    }

    @DisplayName("공격 / 블러핑")
    @Test
    void lyingAttack() {
        // given
        GameAttackRequest attackRequest = new GameAttackRequest();
        attackRequest.setAttacker(new AttackPlayerDto(1L));
        attackRequest.setDefender(new DefendPlayerDto(2L));
        attackRequest.setWeapon(Weapon.TWIN);
        attackRequest.setHand(Hand.RIGHT);
        given(gameRedisRepository.getInGamePlayer(1L, 1L)).willReturn(Optional.of(attacker));
        given(gameRedisRepository.getInGamePlayer(1L, 2L)).willReturn(Optional.of(defender));

        // when
        gameAttackDefenseService.attack(1L, 1L, attackRequest);
        // then
        assertTrue(inGame.getTurnData().isLyingAttack());
    }

    @DisplayName("공격 / 공격 요청 id와 로그인한 사용자 id가 다른 경우")
    @Test
    void BadAttackRequest() {
        // given
        GameAttackRequest attackRequest = new GameAttackRequest();
        attackRequest.setAttacker(new AttackPlayerDto(2L));

        // when
        // then
        assertThrows(CustomWebSocketException.class, () -> gameAttackDefenseService.attack(1L, 1L, attackRequest));
    }

    @DisplayName("공격 조회")
    @Test
    void getAttackInfo() {
        //  given
        AttackData attackData = AttackData.builder()
                .hand(Hand.RIGHT)
                .weapon(Weapon.TWIN)
                .build();

        inGame.getTurnData().setAttackData(attackData);

        //  when
        AttackResponse attackResponse = gameAttackDefenseService.getAttackInfo(1L);
        //  then
        assertEquals(4L, attackResponse.getAttacker().getMemberId());
        assertEquals(1L, attackResponse.getDefender().getMemberId());
        assertEquals(Weapon.TWIN.name(), attackResponse.getWeapon());
        assertEquals(Hand.RIGHT.name(), attackResponse.getHand());
    }

    @DisplayName("공격 패스 / 공격상태에서 패스요청")
    @Test
    void isAttackPass() {
        //  given
        GameAttackPassRequest gameAttackPassRequest = new GameAttackPassRequest();
        gameAttackPassRequest.setAttacker(new AttackPlayerDto(1L));
        inGame.changeStatus(GameStatus.ATTACK);

        //  when
        gameAttackDefenseService.isAttackPass(1L, gameAttackPassRequest, 1L);

        //  then

    }

    @DisplayName("공격 패스 / 공격 외 상태에서 패스요청")
    @Test
    void isNotAttackPass() {
        // given
        //  given
        GameAttackPassRequest gameAttackPassRequest = new GameAttackPassRequest();
        gameAttackPassRequest.setAttacker(new AttackPlayerDto(1L));
        inGame.changeStatus(GameStatus.DEFENSE);
        // when
        // then
        assertThrows(CustomWebSocketException.class, () -> gameAttackDefenseService.isAttackPass(1L, gameAttackPassRequest, 1L));
    }

    @DisplayName("방어 / 손에 들고 있는 방패로 방어")
    @Test
    void defense() {
        //  given
        GameDefenseRequest defenseRequest = new GameDefenseRequest();
        defenseRequest.setDefender(new DefendPlayerDto(2L));
        defenseRequest.setWeapon(Weapon.SHIELD);
        defenseRequest.setHand(Hand.LEFT);

        given(gameRedisRepository.getInGamePlayer(1L, 2L)).willReturn(Optional.of(defender));
        //  when
        gameAttackDefenseService.defense(1L, 2L, defenseRequest);
        //  then
        assertEquals(2L, inGame.getTurnData().getDefenderId());
        assertFalse(inGame.getTurnData().isLyingDefend());
        assertEquals(GameStatus.DEFENSE_DOUBT, inGame.getGameStatus());
    }

    @DisplayName("방어 / 블러핑 방어")
    @Test
    void lyingDefense() {
        GameDefenseRequest defenseRequest = new GameDefenseRequest();
        defenseRequest.setDefender(new DefendPlayerDto(2L));
        defenseRequest.setWeapon(Weapon.SHIELD);
        defenseRequest.setHand(Hand.RIGHT);

        given(gameRedisRepository.getInGamePlayer(1L, 2L)).willReturn(Optional.of(defender));
        //  when
        gameAttackDefenseService.defense(1L, 2L, defenseRequest);
        //  then
        assertTrue(inGame.getTurnData().isLyingDefend());
    }
    @DisplayName("방어 조회")
    @Test
    void getDefenseInfo() {
        //  given
        DefendData defendData = new DefendData(Hand.LEFT, 3);
        inGame.getTurnData().setDefendData(defendData);
        //  when
        DefenseResponse defenseResponse = gameAttackDefenseService.getDefenseInfo(1L);
        //  then
        assertEquals(1L, defenseResponse.getDefender().getMemberId());
        assertEquals(Weapon.SHIELD.name(), defenseResponse.getWeapon());
        assertEquals(Hand.LEFT.name(), defenseResponse.getHand());

    }

    @DisplayName("방어 패스 / 방어 상태에서 패스요청")
    @Test
    void isDefensePass() {
        //  given
        DefendPlayerDto defendPlayerDto = new DefendPlayerDto(2L);
        GameDefensePassRequest defensePassRequest = new GameDefensePassRequest();
        defensePassRequest.setDefender(defendPlayerDto);

        inGame.changeStatus(GameStatus.DEFENSE);

        DefendData defendData = new DefendData(Hand.LEFT, 3);
        inGame.getTurnData().setDefendData(defendData);
        //  when
        gameAttackDefenseService.isDefensePass(1L, defensePassRequest, 2L);
        //  then
        assertTrue(inGame.getTurnData().getDefendData().isDefendPass());
        assertEquals(GameStatus.EXECUTE, inGame.getGameStatus());
    }


    @DisplayName("방어 패스 / 방어 외 상태에서 방어 패스요청")
    @Test
    void isNotDefensePass() {
        //  given
        DefendPlayerDto defendPlayerDto = new DefendPlayerDto(2L);
        GameDefensePassRequest defensePassRequest = new GameDefensePassRequest();
        defensePassRequest.setDefender(defendPlayerDto);

        inGame.changeStatus(GameStatus.ATTACK);
        //  when
        //  then
        assertThrows(CustomWebSocketException.class, () -> gameAttackDefenseService.isDefensePass(1L, defensePassRequest, 2L));

    }
}