package com.a301.theknight.domain.game.repository;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.TeamInfoData;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameRedisRepositoryTest {

    @Autowired
    GameRedisRepository redisRepository;

    long gameId = 1L;
    InGame inGame;

    @BeforeEach
    void setup() {
        inGame = InGame.builder()
                .gameStatus(GameStatus.PREPARE)
                .teamAInfo(TeamInfoData.builder().build())
                .teamBInfo(TeamInfoData.builder().build()).build();
        redisRepository.saveInGame(gameId, inGame);
    }

    @Test
    @DisplayName("InGame 저장 테스트")
    void save() {
        InGame returnInGame = redisRepository.getInGame(gameId).get();

        assertThat(returnInGame.getGameStatus()).isEqualTo(inGame.getGameStatus());
    }

    @Test
    @DisplayName("InGame 변경 테스트")
    void change() {
        InGame changeInGame = InGame.builder()
                .gameStatus(GameStatus.ATTACK)
                .teamAInfo(TeamInfoData.builder().build())
                .teamBInfo(TeamInfoData.builder().build()).build();
        redisRepository.saveInGame(gameId, changeInGame);

        InGame returnInGame = redisRepository.getInGame(gameId).get();

        assertThat(returnInGame.getGameStatus()).isEqualTo(changeInGame.getGameStatus());
    }
}