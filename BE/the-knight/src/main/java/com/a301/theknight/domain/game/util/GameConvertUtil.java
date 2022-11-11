package com.a301.theknight.domain.game.util;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.dto.convert.GameStatusResponse;
import com.a301.theknight.domain.game.dto.convert.PostfixDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.a301.theknight.domain.game.entity.GameStatus.*;
import static com.a301.theknight.domain.game.entity.GameStatus.DEFENSE;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Service
public class GameConvertUtil {
    private final GameRedisRepository gameRedisRepository;
    private final RedissonClient redissonClient;

    private final Map<String, String> dataPostfixMap;

    public GameConvertUtil(GameRedisRepository gameRedisRepository, RedissonClient redissonClient) {
        this.gameRedisRepository = gameRedisRepository;
        this.redissonClient = redissonClient;
        dataPostfixMap = new HashMap<>();
        init(dataPostfixMap);
    }

    private void init(Map<String, String> postfixMap) {
        postfixMap.put(GameStatus.WAITING.name(), "/entry");
        postfixMap.put(GameStatus.PREPARE.name(), "/prepare");
        postfixMap.put(GameStatus.PREDECESSOR.name(), "/pre-attack");
        postfixMap.put(GameStatus.ATTACK.name(), "/attacker");
        postfixMap.put(GameStatus.ATTACK_DOUBT.name(), "/attack-info");
        postfixMap.put(GameStatus.DEFENSE.name(), "/attack-info");
        postfixMap.put(GameStatus.DEFENSE_DOUBT.name(), "/defense-info");
        postfixMap.put(GameStatus.DOUBT_RESULT.name(), "/doubt-info");
        postfixMap.put(GameStatus.EXECUTE.name(), "/execute");
    }

    @Transactional
    public GameStatusResponse convertScreen(long gameId) {
        InGame inGame = getInGame(gameId);
        inGame.initRequestCount();
        gameRedisRepository.saveInGame(gameId, inGame);

        return new GameStatusResponse(inGame.getGameStatus().name());
    }

    @Transactional
    public GameStatusResponse forceConvertScreen(long gameId) {
        InGame inGame = getInGame(gameId);
        inGame.initRequestCount();
        gameRedisRepository.saveInGame(gameId, inGame);

        GameStatus curStatus = inGame.getGameStatus();
        GameStatus nextStatus = nextStatus(curStatus);

        return new GameStatusResponse(nextStatus.name());
    }

    @Transactional
    public PostfixDto completeConvertPrepare(long gameId) {
        boolean isFullCount;
        GameStatus gameStatus;

        RLock lock = redissonClient.getLock(generateConvertLockKey(gameId));
        try {
            boolean available = lock.tryLock(5, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new CustomRestException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
            }

            InGame inGame = getInGame(gameId);
            inGame.addRequestCount();
            gameRedisRepository.saveInGame(gameId, inGame);

            isFullCount = inGame.isFullCount();
            gameStatus = inGame.getGameStatus();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

        if (isFullCount) {
            RLock timeLock = redissonClient.getLock(generateTimeLock(gameId));
            try {
                boolean isGetTimeLock = timeLock.tryLock(5, 2, TimeUnit.SECONDS);
                if (!isGetTimeLock) {
                    throw new CustomWebSocketException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
                }
                return new PostfixDto(getPostfix(gameStatus));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                timeLock.unlock();
            }
        }
        return null;
    }

    public GameStatus getGameStatus(long gameId) {
        return getInGame(gameId).getGameStatus();
    }

    private String getPostfix(GameStatus gameStatus) {
        return dataPostfixMap.get(gameStatus.name());
    }

    private String generateTimeLock(long gameId) {
        return "time_lock:" + gameId;
    }

    private String generateConvertLockKey(long gameId) {
        return "convert_game:" + gameId;
    }

    private GameStatus nextStatus(GameStatus curStatus) {
        switch (curStatus) {
            case PREPARE:
                return PREDECESSOR;
            case ATTACK:
                return ATTACK;
            case DEFENSE: case DEFENSE_DOUBT:
                return EXECUTE;
            case ATTACK_DOUBT:
                return DEFENSE;
        }
        return null;
    }

    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }
}
