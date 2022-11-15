package com.a301.theknight.domain.game.util;

import com.a301.theknight.domain.game.dto.convert.PostfixDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GameConvertUtilTest {

    @Mock
    GameRedisRepository gameRedisRepository;

    @Autowired
    RedissonClient redissonClient;

    long gameId = 1L;
    int peopleNum = 100;
    GameConvertUtil gameConvertUtil;
    InGame inGame;

    @BeforeEach
    void setup() {
        gameConvertUtil = new GameConvertUtil(gameRedisRepository, redissonClient);
        inGame = InGame.builder()
                .gameStatus(GameStatus.ATTACK)
                .build();
        inGame.initRequestCount();
    }

    @Test
    @DisplayName("전환 완료 카운팅 동시성 테스트")
    void convertCompleteCountTest() throws Exception {
        //given
        given(gameRedisRepository.getInGame(gameId)).willReturn(Optional.of(inGame));
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(peopleNum - 1);
        //when
        for (int i = 0; i < peopleNum - 1; i++) {
            executorService.submit(() -> {
                try {
//                    gameConvertUtil.completeConvertPrepare(gameId);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        //then
        assertThat(inGame.getRequestCount()).isEqualTo(peopleNum - 1);
    }

    @Test
    @DisplayName("모든 플레이어 전환 완료 후 응답 테스트")
    void completeResponseTest() throws Exception {
        //given
        given(gameRedisRepository.getInGame(gameId)).willReturn(Optional.of(inGame));
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(peopleNum - 1);
        //when
        for (int i = 0; i < peopleNum - 1; i++) {
            executorService.submit(() -> {
                try {
//                    gameConvertUtil.completeConvertPrepare(gameId);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

//        PostfixDto postfixDto = gameConvertUtil.completeConvertPrepare(gameId);
        //then
        assertThat(inGame.getRequestCount()).isEqualTo(peopleNum);
        assertThat(inGame.isFullCount()).isTrue();
//        assertThat(postfixDto.getPostfix()).isEqualTo("/attacker");
    }
}