package com.a301.theknight.domain.game.util;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Component
public class TimeLimitUtil {
    private final GameRedisRepository redisRepository;
    private final SendMessageService sendMessageService;
    private final RedissonClient redissonClient;

    private final Map<String, Long> limitTimeMap;

    public TimeLimitUtil(GameRedisRepository redisRepository, SendMessageService sendMessageService, RedissonClient redissonClient) {
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

    public void timeLimit(long gameId, GameStatus nextStatus) {
        GameStatus preStatus = getInGame(gameId).getGameStatus();
        try {
            Thread.sleep(limitTimeMap.get(preStatus.name()));
            InGame curInGame = getInGame(gameId);
            if (!preStatus.equals(curInGame.getGameStatus())) {
                return;
            }

            curInGame.changeStatus(nextStatus);
            redisRepository.saveInGame(gameId, curInGame);
            RLock lock = redissonClient.getLock(timeLockKeyGen(gameId));
            lock.tryLock(1, 1, TimeUnit.SECONDS);
            // /convert 요청
            sendMessageService.convertCall(gameId);
            //TODO: [강제 로직 수행~~] -> curStatus에 맞게 로직을 수행해야 함.
            // 각 Status별 강제 수행 로직 정리하기.
            // 로직 수행하면서 Status를 nextStatus로 바꿔주는 방식도 좋을듯??

            //Timelock 해제
            lock.unlock();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private String timeLockKeyGen(long gameId) {
        return "time_lock:" + gameId;
    }
}
