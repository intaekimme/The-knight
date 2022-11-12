package com.a301.theknight.domain.limit.template;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@RequiredArgsConstructor
@Component
public abstract class TimeLimitServiceTemplate {
    private final GameRedisRepository redisRepository;
    private final RedissonClient redissonClient;

    public final boolean executeTimeLimit(long gameId, SendMessageService sendMessageService) {
        GameStatus preStatus = getInGame(gameId).getGameStatus();

        RLock timeLock = null;
        try {
            Thread.sleep(preStatus.getLimitSeconds());
            InGame curInGame = getInGame(gameId);
            if (!preStatus.equals(curInGame.getGameStatus())) {
                return false;
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
        return true;
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
