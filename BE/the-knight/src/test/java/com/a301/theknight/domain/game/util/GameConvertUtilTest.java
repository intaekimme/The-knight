package com.a301.theknight.domain.game.util;

import com.a301.theknight.domain.game.dto.prepare.response.GameOrderDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.player.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GameConvertUtilTest {

    @Autowired
    GameRedisRepository gameRedisRepository;

    @Autowired
    GameConvertUtil gameConvertUtil;

    @Autowired
    RedissonClient redissonClient;

    long gameId = 10000000L;
    int peopleNum = 10;
    InGame inGame;

    @BeforeEach
    void setup() {
        gameRedisRepository.deleteInGame(gameId);
        gameConvertUtil.initRequestQueue(gameId, peopleNum);
    }

    @Test
    @DisplayName("Screen-Data Counting Test")
//    @Transactional
    void requestCountTest() throws Exception {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(peopleNum - 1);
        gameConvertUtil.initRequestQueue(gameId);
        //when
        long start = System.currentTimeMillis();
        for (int i = 0; i < peopleNum - 1; i++) {
            executorService.submit(() -> {
                try {
                    boolean isFull = gameConvertUtil.requestCounting(gameId);
                    assertThat(isFull).isFalse();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        boolean isFull = gameConvertUtil.requestCounting(gameId);
        System.out.printf("==== %.3f Second ====\n", (double) (System.currentTimeMillis() - start) / 1000);
        //then
        assertThat(isFull).isTrue();
//        assertThat(countInGame.getRequestCount()).isEqualTo(peopleNum - 1);
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

    private void initInGameData() {
        TeamInfoData teamInfo = makeTeamInfoData();

        inGame = InGame.builder()
                .gameStatus(GameStatus.ATTACK)
                .currentAttackTeam(Team.A)
                .maxMemberNum(peopleNum)
                .teamAInfo(teamInfo)
                .teamBInfo(teamInfo)
                .turnData(makeTurnData()).build();
        inGame.initRequestCount();
        gameRedisRepository.saveInGame(gameId, inGame);
    }

    private TeamInfoData makeTeamInfoData() {
        int num = peopleNum / 2;

        return TeamInfoData.builder()
                .currentAttackIndex(num - 1)
                .orderList(new GameOrderDto[num])
                .leaderId(0).build();
    }

    private TurnData makeTurnData() {
        TurnData turnData = new TurnData();
        turnData.setAttackData(AttackData.builder().build());
        turnData.setDefenseData(DefendData.builder().build());
        turnData.setDoubtData(DoubtData.builder().build());

        return turnData;
    }
}