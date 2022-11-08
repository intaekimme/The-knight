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
        try {
            Thread.sleep(limitTimeMap.get(preStatus.name()));
            InGame curInGame = getInGame(gameId);
            if (!preStatus.equals(curInGame.getGameStatus())) {
                return;
            }

            //convert 때문에 얘를 저장하는데, 서버에 /force-convert 를 따로 만들면 될 듯
            //거기는 nextStatus를 인자로 받아서 그걸 바로 프론트에 리턴해주는 방식
//            curInGame.changeStatus(nextStatus);
//            redisRepository.saveInGame(gameId, curInGame);

            RLock lock = redissonClient.getLock(timeLockKeyGen(gameId));
            if (!lock.tryLock(1, 1, TimeUnit.SECONDS)) {
                throw new CustomWebSocketException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
            }
            // /convert 요청
            //TODO: force convert 받는 API 짜기
            sendMessageService.forceConvertCall(gameId);

            runLimitLogic(gameId, curInGame);

            //TODO: [강제 로직 수행~~] -> curStatus에 매핑된 force logic을 수행
            // Message를 보내주어 수행? -> 해당 메소드가 끝나고 timeLock을 해제해주어야 함.
            // 각 Status별 강제 수행 로직 정리하기.
            // 로직 수행하면서 Status를 nextStatus로 바꿔주는 방식도 좋을듯??

            //Timelock 해제
            lock.unlock();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
