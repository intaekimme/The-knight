package com.a301.theknight.domain.game.service;

import com.a301.theknight.domain.game.dto.convert.GameStatusResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.a301.theknight.domain.game.entity.GameStatus.*;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_PLAYER_IS_NOT_EXIST;

@RequiredArgsConstructor
@Service
public class GameConvertService {
    private final GameRedisRepository gameRedisRepository;
    private final GameConvertUtil gameConvertUtil;
    private final RedissonClient redissonClient;

    @Transactional
    public List<String> convertComplete(long gameId) {
        boolean isFullCount;
        String gameStatus;

        RLock lock = redissonClient.getLock(lockKeyGen(gameId));
        try {
            boolean available = lock.tryLock(5, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new CustomRestException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
            }

            InGame inGame = getInGame(gameId);
            inGame.addRequestCount();
            gameRedisRepository.saveInGame(gameId, inGame);

            isFullCount = inGame.isFullCount();
            gameStatus = inGame.getGameStatus().name();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

        if (isFullCount) {
            RLock timeLock = redissonClient.getLock(timeLockKeyGen(gameId));
            try {
                boolean isGetTimeLock = timeLock.tryLock(5, 2, TimeUnit.SECONDS);
                if (!isGetTimeLock) {
                    throw new CustomWebSocketException(DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK);
                }
                return gameConvertUtil.getPostfixList(gameStatus);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                timeLock.unlock();
            }
        }
        return null;
    }

    @Transactional
    public GameStatusResponse getGameStatus(long gameId) {
        InGame inGame = getInGame(gameId);
        inGame.initRequestCount();
        gameRedisRepository.saveInGame(gameId, inGame);

        return new GameStatusResponse(inGame.getGameStatus().name());
    }

    @Transactional
    public GameStatusResponse getNextGameStatus(long gameId) {
        InGame inGame = getInGame(gameId);
        inGame.initRequestCount();
        gameRedisRepository.saveInGame(gameId, inGame);

        GameStatus curStatus = inGame.getGameStatus();
        GameStatus nextStatus = nextStatus(curStatus);

        return new GameStatusResponse(nextStatus.name());
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

    private InGamePlayer getInGamePlayer(long gameId, long memberId) {
        return gameRedisRepository.getInGamePlayer(gameId, memberId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));
    }

    private String lockKeyGen(long gameId) {
        return "game:" + gameId + "_convert_lock";
    }

    private String timeLockKeyGen(long gameId) {
        return "time_lock:" + gameId;
    }

}
