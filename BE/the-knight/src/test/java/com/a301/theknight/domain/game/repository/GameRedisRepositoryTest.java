package com.a301.theknight.domain.game.repository;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.player.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameRedisRepositoryTest {

    @Autowired
    GameRedisRepository redisRepository;

    long gameId = 1L;
    long memberId = 10L;
    InGame inGame;
    InGamePlayer inGamePlayer;
    GameWeaponData gameWeaponData;

    @BeforeEach
    void setup() {
        inGame = InGame.builder()
                .gameStatus(GameStatus.PREPARE)
                .teamAInfo(TeamInfoData.builder().build())
                .teamBInfo(TeamInfoData.builder().build()).build();
        redisRepository.saveInGame(gameId, inGame);

        gameWeaponData = GameWeaponData.builder()
                .twin(2)
                .sword(2)
                .shield(2)
                .hand(2).build();
        redisRepository.saveGameWeaponData(gameId, Team.A, gameWeaponData);

        inGamePlayer = InGamePlayer.builder()
                .memberId(memberId)
                .nickname("nickname")
                .build();
        redisRepository.saveInGamePlayer(gameId, memberId, inGamePlayer);
    }

    @Test
    @DisplayName("InGame 저장 테스트")
    void save() {
        InGame returnInGame = redisRepository.getInGame(gameId).get();

        assertThat(returnInGame.getGameStatus()).isEqualTo(inGame.getGameStatus());
    }

    @Test
    @DisplayName("InGame 수정 테스트")
    void change() {
        InGame changeInGame = InGame.builder()
                .gameStatus(GameStatus.ATTACK)
                .teamAInfo(TeamInfoData.builder().build())
                .teamBInfo(TeamInfoData.builder().build()).build();
        redisRepository.saveInGame(gameId, changeInGame);

        InGame returnInGame = redisRepository.getInGame(gameId).get();

        assertThat(returnInGame.getGameStatus()).isEqualTo(changeInGame.getGameStatus());
    }

    @Test
    @DisplayName("InGamePlayer 저장 테스트")
    void savePlayer() {
        InGamePlayer returnPlayer = redisRepository.getInGamePlayer(gameId, memberId).get();

        assertThat(returnPlayer.getNickname()).isEqualTo(inGamePlayer.getNickname());
    }

    @Test
    @DisplayName("InGamePlayer List 저장 테스트")
    void savePlayerList() {
        List<InGamePlayer> inGamePlayerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            inGamePlayerList.add(InGamePlayer.builder()
                    .nickname("nickname" + i)
                    .memberId(i + 2L)
                    .build());
        }
        redisRepository.saveInGamePlayerAll(gameId, inGamePlayerList);

        for (int i = 0; i < 10; i++) {
            InGamePlayer returnPlayer = redisRepository.getInGamePlayer(gameId, i + 2L).get();
            assertThat(returnPlayer.getNickname()).isEqualTo("nickname" + i);
        }
    }

    @Test
    @DisplayName("InGamePlayer List 수정 테스트")
    void changePlayerList() {
        List<InGamePlayer> inGamePlayerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            inGamePlayerList.add(InGamePlayer.builder()
                    .nickname("nickname" + i)
                    .memberId(i + 2L)
                    .build());
        }
        redisRepository.saveInGamePlayerAll(gameId, inGamePlayerList);

        List<InGamePlayer> inGamePlayerList2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            inGamePlayerList2.add(InGamePlayer.builder()
                    .nickname("change" + i)
                    .memberId(i + 2L)
                    .build());
        }
        redisRepository.saveInGamePlayerAll(gameId, inGamePlayerList2);

        for (int i = 0; i < 10; i++) {
            InGamePlayer returnPlayer = redisRepository.getInGamePlayer(gameId, i + 2L).get();
            assertThat(returnPlayer.getNickname()).isEqualTo("change" + i);
        }
    }

    @Test
    @DisplayName("InGamePlayer 수정 테스트")
    void changeInGamePlayer() {
        InGamePlayer changePlayer = InGamePlayer.builder()
                .memberId(memberId)
                .nickname("change nickname")
                .build();
        redisRepository.saveInGamePlayer(gameId, memberId, changePlayer);

        InGamePlayer returnPlayer = redisRepository.getInGamePlayer(gameId, memberId).get();

        assertThat(returnPlayer.getNickname()).isEqualTo(changePlayer.getNickname());
    }

    @Test
    @DisplayName("GameWeaponData 저장 테스트")
    void saveWeapon() {
        GameWeaponData weaponData = redisRepository.getGameWeaponData(gameId, Team.A).get();

        assertThat(weaponData.getHand()).isEqualTo(gameWeaponData.getHand());
        assertThat(weaponData.getSword()).isEqualTo(gameWeaponData.getSword());
        assertThat(weaponData.getTwin()).isEqualTo(gameWeaponData.getTwin());
        assertThat(weaponData.getShield()).isEqualTo(gameWeaponData.getShield());
    }

    @Test
    @DisplayName("GameWeaponData 수정 테스트")
    void changeWeapon() {
        GameWeaponData changeData = GameWeaponData.builder()
                .twin(3)
                .sword(3)
                .shield(3)
                .hand(3).build();
        redisRepository.saveGameWeaponData(gameId, Team.A, changeData);

        GameWeaponData weaponData = redisRepository.getGameWeaponData(gameId, Team.A).get();

        assertThat(weaponData.getHand()).isEqualTo(changeData.getHand());
        assertThat(weaponData.getSword()).isEqualTo(changeData.getSword());
        assertThat(weaponData.getTwin()).isEqualTo(changeData.getTwin());
        assertThat(weaponData.getShield()).isEqualTo(changeData.getShield());
    }

    @Test
    @DisplayName("GameWeaponData 삭제 테스트")
    void deleteWeapon() {
        redisRepository.deleteGameWeaponData(gameId, Team.A);

        NullPointerException nullPointerException = assertThrows(NullPointerException.class, () -> {
            GameWeaponData weaponData = redisRepository.getGameWeaponData(gameId, Team.A)
                    .orElseThrow(() -> new NullPointerException("없습니다."));
        });
        assertThat(nullPointerException.getMessage()).isEqualTo("없습니다.");
    }
}