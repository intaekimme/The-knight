package com.a301.theknight.domain.limit.template;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Component
public abstract class TimeLimitServiceTemplate {
    private final GameRedisRepository redisRepository;
    private final SendMessageService sendMessageService;
    private final RedissonClient redissonClient;

    private final Map<String, Long> limitTimeMap;

    public TimeLimitServiceTemplate(GameRedisRepository redisRepository, SendMessageService sendMessageService, RedissonClient redissonClient) {
        this.redisRepository = redisRepository;
        this.sendMessageService = sendMessageService;
        this.redissonClient = redissonClient;

        limitTimeMap = new HashMap<>();
        initLimitTime();
    }

    private void initLimitTime() {
        limitTimeMap.put(GameStatus.PREPARE.name(), 100L);
        limitTimeMap.put(GameStatus.ATTACK.name(), 60L);
    }

    public final void timeLimit(long gameId, GameStatus nextStatus) {
        GameStatus preStatus = getInGame(gameId).getGameStatus();

        RLock timeLock = null;
        try {
            Thread.sleep(limitTimeMap.get(preStatus.name()));
            InGame curInGame = getInGame(gameId);
            if (!preStatus.equals(curInGame.getGameStatus())) {
                return;
            }

            timeLock = redissonClient.getLock(timeLockKeyGen(gameId));
            if (!timeLock.tryLock(1, 1, TimeUnit.SECONDS)) {
                throw new CustomWebSocketException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
            }
            sendMessageService.forceConvertCall(gameId);

            runLimitLogic(gameId, curInGame);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            timeLock.unlock();
        }
    }

    public abstract void runLimitLogic(long gameId, InGame inGame);

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private String timeLockKeyGen(long gameId) {
        return "time_lock:" + gameId;
    }
}
