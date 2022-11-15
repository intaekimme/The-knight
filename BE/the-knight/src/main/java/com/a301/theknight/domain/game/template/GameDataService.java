package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public abstract class GameDataService {

    private final RedissonClient redissonClient;

    public GameDataService(RedissonClient redissonClient) {
        log.info("GameDataService Creator RedissonClient = {}", redissonClient);
        this.redissonClient = redissonClient;
    }

    public final void makeAndSendData(long gameId, SendMessageService messageService) {
        log.info("[Redisson Autowired Check] {}", redissonClient);
        RLock dataLock = redissonClient.getLock(dataLockKeyGen(gameId));
        try {
            tryDataLock(dataLock);

            makeData(gameId);
            sendScreenData(gameId, messageService);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unLock(dataLock);
        }
    }

    protected abstract void makeData(long gameId);

    protected abstract void sendScreenData(long gameId, SendMessageService messageService);

    private void unLock(RLock timeLock) {
        if (timeLock != null && timeLock.isLocked()) {
            timeLock.unlock();
        }
    }

    private void tryDataLock(RLock timeLock) throws InterruptedException {
        if (!timeLock.tryLock(1, 1, TimeUnit.SECONDS)) {
            throw new CustomWebSocketException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
        }
    }

    private String dataLockKeyGen(long gameId) {
        return "data_lock:" + gameId;
    }
}
