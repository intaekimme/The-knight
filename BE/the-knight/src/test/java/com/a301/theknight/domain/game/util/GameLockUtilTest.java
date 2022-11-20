package com.a301.theknight.domain.game.util;

import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameLockUtilTest {

    @Autowired
    GameLockUtil gameLockUtil;

    int peopleNum = 10;

    @Test
    @DisplayName("다른 쓰레드 Lock 테스트")
    void otherThreadLockTest() throws Exception {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //when
        executorService.submit(() -> {
            gameLockUtil.dataLock(1, 1, 6);
        });
        executorService.submit(() -> {
            assertThrows(CustomWebSocketException.class, () -> gameLockUtil.dataLock(1, 3, 5));
        });
    }

    @Test
    @DisplayName("다른 쓰레드 UnLock 테스트")
    void otherThreadUnLockTest() throws Exception {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //when
        executorService.submit(() -> {
            gameLockUtil.dataLock(1, 1, 6);
        });
        executorService.submit(() -> {
            gameLockUtil.dataUnLock(1);
            gameLockUtil.dataLock(1, 3, 5);
        });
    }
}