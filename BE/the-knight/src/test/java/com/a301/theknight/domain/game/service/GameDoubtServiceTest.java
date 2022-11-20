package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.doubt.response.DoubtResponseDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameDoubtServiceTest {

    @Mock
    GameRedisRepository gameRedisRepository;
    GameDoubtService gameDoubtService;
    @Mock
    RedissonClient redissonClient;
    @Mock
    RLock lock;
    private InGame inGame;
    private List<InGamePlayer> suspects;
    private InGamePlayer suspected;
    private DoubtData doubtData;

    @BeforeEach
    void setup() {
        TurnData turnData = new TurnData();
        turnData.setAttackData(AttackData.builder()
                .hand(Hand.LEFT)
                .weapon(Weapon.SWORD)
                .build());

        inGame = InGame.builder()
                .gameStatus(GameStatus.ATTACK_DOUBT)
                .currentAttackTeam(Team.A)
                .turnData(turnData)
                .build();

        suspects = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            suspects.add(i, InGamePlayer.builder()
                    .memberId((long)(i))
                    .team(Team.A)
                    .leader(false)
                    .build());
        }

        suspected = InGamePlayer.builder()
                .memberId(4L)
                .team(Team.B)
                .leader(false)
                .build();
        suspected.randomChoiceWeapon(Weapon.SWORD);
        suspected.randomChoiceWeapon(Weapon.TWIN);

        doubtData = DoubtData.builder()
                .suspectId(0)
                .suspectedId(4L)
                .doubtStatus(DoubtStatus.ATTACK)
                .doubtHand(Hand.LEFT)
                .build();
        turnData.setDoubtData(doubtData);
        inGame.recordTurnData(turnData);


//        gameDoubtService = new GameDoubtService(gameRedisRepository, redissonClient);
    }

    @Test
    void doubtPass() throws InterruptedException {
        given(gameRedisRepository.getInGame(1L)).willReturn(Optional.of(inGame));
        given(gameRedisRepository.getInGamePlayer(1L, 0)).willReturn(Optional.of(suspects.get(0)));
        given(gameRedisRepository.getInGamePlayer(1L, 1L)).willReturn(Optional.of(suspects.get(1)));
        given(gameRedisRepository.getInGamePlayer(1L, 2L)).willReturn(Optional.of(suspects.get(2)));

        given(redissonClient.getLock("game:1_convert_lock")).willReturn(lock);
        given(lock.tryLock(5, 2, TimeUnit.SECONDS)).willReturn(true);
        given(gameRedisRepository.saveInGame(1L, inGame)).willReturn(inGame);

//        given(gameRedisRepository.getInGamePlayerList(1L)).willReturn(suspects);
        given(gameRedisRepository.getTeamPlayerList(1L ,Team.A)).willReturn(suspects);

        gameDoubtService.doubtPass(1L, 0);
        gameDoubtService.doubtPass(1L, 1L);
        gameDoubtService.doubtPass(1L, 2L);

        assertEquals(GameStatus.DEFENSE, inGame.getGameStatus());

    }

    @Test
    void validAttackDoubt() {
        given(gameRedisRepository.getInGame(1L)).willReturn(Optional.of(inGame));
        given(gameRedisRepository.getInGamePlayer(1L, 0)).willReturn(Optional.of(suspects.get(0)));
        given(gameRedisRepository.getInGamePlayer(1L, 4L)).willReturn(Optional.of(suspected));


        gameDoubtService.doubt(1L, 0, 4L, GameStatus.ATTACK_DOUBT);

        assertEquals(0, inGame.getTurnData().getDoubtData().getSuspectId());
        assertEquals(4L, inGame.getTurnData().getDoubtData().getSuspectedId());
        assertFalse(inGame.getTurnData().getDoubtData().isDoubtSuccess());
        assertEquals(DoubtStatus.ATTACK, inGame.getTurnData().getDoubtData().getDoubtStatus());
    }


    @Test
    void getDoubtInfo() {
        given(gameRedisRepository.getInGame(1L)).willReturn(Optional.of(inGame));
        given(gameRedisRepository.getInGamePlayer(1L, 0)).willReturn(Optional.of(suspects.get(0)));
        given(gameRedisRepository.getInGamePlayer(1L, 4L)).willReturn(Optional.of(suspected));

        DoubtResponseDto doubtResponseDto =  gameDoubtService.getDoubtInfo(1L);

        assertFalse(doubtResponseDto.getDoubtResponse().isDoubtSuccess());
        assertEquals(GameStatus.DEFENSE, doubtResponseDto.getDoubtStatus());
        assertEquals("A", doubtResponseDto.getDoubtResponse().getDoubtTeam());
    }


}