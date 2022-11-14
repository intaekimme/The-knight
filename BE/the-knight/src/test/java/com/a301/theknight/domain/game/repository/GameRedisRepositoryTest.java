package com.a301.theknight.domain.game.repository;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.player.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}