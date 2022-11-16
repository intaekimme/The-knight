package com.a301.theknight.domain.limit.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

public abstract class TimeLimitServiceTemplate {

    private GameRedisRepository redisRepository;
    private RedissonClient redissonClient;


    public TimeLimitServiceTemplate(GameRedisRepository redisRepository, RedissonClient redissonClient) {
        this.redisRepository = redisRepository;
        this.redissonClient = redissonClient;
    }

    @Transactional
    public void executeTimeLimit(long gameId, SendMessageService sendMessageService) {
        GameStatus preStatus = getInGame(gameId).getGameStatus();

        RLock dataLock = null;
        try {
            Thread.sleep(preStatus.getLimitMilliSeconds());
            InGame curInGame = getInGame(gameId);
            if (!preStatus.equals(curInGame.getGameStatus())) {
                return;
            }

            dataLock = redissonClient.getLock(dataLockKeyGen(gameId));
            tryDataLock(dataLock);
            sendMessageService.forceConvertCall(gameId);

            runLimitLogic(gameId, curInGame);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unLock(dataLock);
        }
    }

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

    public abstract void runLimitLogic(long gameId, InGame inGame);

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private String dataLockKeyGen(long gameId) {
        return "data_lock:" + gameId;
    }
}
