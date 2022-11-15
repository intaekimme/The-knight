package com.a301.theknight.domain.game.util;

import com.a301.theknight.domain.game.dto.convert.ConvertResponse;
import com.a301.theknight.domain.game.dto.convert.PostfixDto;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.errorcode.DomainErrorCode;
import com.a301.theknight.global.error.errorcode.GamePlayingErrorCode;
import com.a301.theknight.global.error.exception.CustomRestException;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.a301.theknight.domain.game.entity.GameStatus.*;
import static com.a301.theknight.global.error.errorcode.DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameConvertUtil {
    private final GameRedisRepository gameRedisRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public ConvertResponse convertScreen(long gameId) {
        InGame inGame = getInGame(gameId);
        inGame.initRequestCount();
        gameRedisRepository.saveInGame(gameId, inGame);

        GameStatus gameStatus = inGame.getGameStatus();
        return new ConvertResponse(gameStatus.name());
    }

    @Transactional
    public ConvertResponse forceConvertScreen(long gameId) {
        InGame inGame = getInGame(gameId);
        inGame.initRequestCount();
        gameRedisRepository.saveInGame(gameId, inGame);

        GameStatus curStatus = inGame.getGameStatus();
        GameStatus nextStatus = nextStatus(curStatus);

        return new ConvertResponse(nextStatus.name());
    }

    @Transactional
    public boolean requestCounting(long gameId) {
        RLock countLock = redissonClient.getLock(generateCountKey(gameId));
        try {
            tryAcquireCountLock(countLock);

            InGame inGame = getInGame(gameId);
            inGame.addRequestCount();
            gameRedisRepository.saveInGame(gameId, inGame);
            log.info("  Request Counting = {}", inGame.getRequestCount());

            return inGame.isFullCount();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unLock(countLock);
        }
    }

    public GameStatus getGameStatus(long gameId) {
        return getInGame(gameId).getGameStatus();
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
        throw new CustomWebSocketException(GamePlayingErrorCode.WRONG_GAME_STATUS);
    }

    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }

    private void tryAcquireCountLock(RLock countLock) throws InterruptedException {
        if (!countLock.tryLock(5, 1, TimeUnit.SECONDS)) {
            throw new CustomWebSocketException(FAIL_TO_ACQUIRE_REDISSON_LOCK);
        }
    }

    private void unLock(RLock countLock) {
        if (countLock != null && countLock.isLocked()) {
            countLock.unlock();
        }
    }

    private String generateCountKey(long gameId) {
        return "game_count_lock:" + gameId;
    }
}
