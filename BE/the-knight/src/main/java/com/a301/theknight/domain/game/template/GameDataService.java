package com.a301.theknight.domain.game.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class GameDataService {

    private RedissonClient redissonClient;

    public GameDataService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Transactional
    public void makeAndSendData(long gameId, SendMessageService messageService) {
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

    private void unLock(RLock dataLock) {
        if (dataLock != null && dataLock.isLocked()) {
            dataLock.unlock();
        }
    }

    private void tryDataLock(RLock dataLock) throws InterruptedException {
        if (!dataLock.tryLock(1, 1, TimeUnit.SECONDS)) {
            throw new CustomWebSocketException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
        }
    }

    private String dataLockKeyGen(long gameId) {
        return "data_lock:" + gameId;
    }
}
