package com.a301.theknight.domain.game.util;

import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.a301.theknight.global.error.errorcode.DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK;

@Slf4j
@RequiredArgsConstructor
@Component
public class GameLockUtil {

    private final RedissonClient redissonClient;

    public void dataLock(long gameId, long waitTime, long leaseTime) {
        String lockKey = generateDataLock(gameId);
        log.info("===== Data Lock Key = {} =====", lockKey);
        if (!tryLock(lockKey, waitTime, leaseTime)) {
            throw new CustomWebSocketException(FAIL_TO_ACQUIRE_REDISSON_LOCK);
        }
    }

    public void dataUnLock(long gameId) {
        String dataLock = generateDataLock(gameId);
        log.info("===== Data UnLock Key = {} =====", dataLock);
        unLock(dataLock);
    }

    public boolean countLock(long gameId, long waitTime, long leaseTime) {
        String lockKey = generateCountLockKey(gameId);
        return tryLock(lockKey, waitTime, leaseTime);
    }

    public void countUnLock(long gameId) {
        String countLock = generateCountLockKey(gameId);
        unLock(countLock);
    }

    public boolean doubtPassLock(long gameId, long waitTime, long leaseTime) {
        String lockKey = generatePassLockKey(gameId);
        return tryLock(lockKey, waitTime, leaseTime);
    }

    public void doubtPassUnLock(long gameId) {
        String lockKey = generatePassLockKey(gameId);
        unLock(lockKey);
    }

    public boolean clickLock(long gameId, long memberId, String MethodName) {
        String lockKey = generateClickLock(gameId, memberId, MethodName);
        log.info(" Prevent Lock Key = {}", lockKey);
        return tryLock(lockKey, 1, 7);
    }
//
//    public void clickUnLock(long gameId, long memberId) {
//        String lockKey = generateClickLock(gameId, memberId, MethodName);
//        unLock(lockKey);
//    }

    private boolean tryLock(String key, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void unLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock != null && lock.isLocked()) {
            lock.unlock();
        }
    }

    private String generateClickLock(long gameId, long memberId, String methodName) {
        return methodName + "_click_lock:" + gameId + "_" + memberId;
    }

    private String generateDataLock(long gameId) {
        return "data_lock:" + gameId;
    }

    private String generateCountLockKey(long gameId) {
        return "game_count_lock:" + gameId;
    }

    public String generatePassLockKey(long gameId) {
        return "game_doubt_pass:" + gameId;
    }
}
