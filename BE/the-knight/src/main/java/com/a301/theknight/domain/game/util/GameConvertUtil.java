package com.a301.theknight.domain.game.util;

import com.a301.theknight.domain.game.dto.convert.ConvertResponse;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.*;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.a301.theknight.domain.game.entity.GameStatus.*;
import static com.a301.theknight.global.error.errorcode.DomainErrorCode.FAIL_TO_ACQUIRE_REDISSON_LOCK;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameConvertUtil {
    private final GameRedisRepository gameRedisRepository;
    private final RedissonClient redissonClient;

    public ConvertResponse convertScreen(long gameId) {
        InGame inGame = getInGame(gameId);

        GameStatus gameStatus = inGame.getGameStatus();
        return new ConvertResponse(gameStatus.name());
    }

    @Transactional
    public ConvertResponse forceConvertScreen(long gameId) {
        InGame inGame = getInGame(gameId);
        GameStatus curStatus = getGameStatus(gameId);

        GameStatus nextStatus = getNextStatus(gameId, inGame, curStatus);
        inGame.changeStatus(nextStatus);
        gameRedisRepository.saveInGame(gameId, inGame);

        return new ConvertResponse(nextStatus.name());
    }

//    @Transactional
    public boolean requestCounting(long gameId) {
        RLock countLock = redissonClient.getLock(generateCountKey(gameId));
        try {
            boolean available = countLock.tryLock(15, 30, TimeUnit.SECONDS);
            if (!available) {
                throw new CustomWebSocketException(FAIL_TO_ACQUIRE_REDISSON_LOCK);
            }

            InGame inGame = getInGame(gameId);
            inGame.addRequestCount();
            log.info("  [{} Counting= {}]", inGame.getGameStatus().name(), inGame.getRequestCount());

            boolean isFullCount = inGame.isFullCount();
            if (isFullCount) {
                inGame.initRequestCount();
            }
            gameRedisRepository.saveInGame(gameId, inGame);
            return isFullCount;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unLock(countLock);
        }
    }

    public GameStatus getGameStatus(long gameId) {
        return getInGame(gameId).getGameStatus();
    }

    public GameStatus getNextStatus(long gameId, InGame inGame, GameStatus gameStatus) {
        switch (gameStatus) {
            case PREPARE:
                return PREDECESSOR;
            case PREDECESSOR: case ATTACK:
                return ATTACK;
            case ATTACK_DOUBT:
                return DEFENSE;
            case DEFENSE: case DEFENSE_DOUBT:
                return EXECUTE;
            case EXECUTE:
                return getStatusAfterExecute(gameId, inGame);
            case DOUBT_RESULT:
                return getStatusAfterDoubt(inGame.getTurnData());
        }
        throw new CustomWebSocketException(WRONG_GAME_STATUS);
    }

    private GameStatus getStatusAfterExecute(long gameId, InGame inGame) {
        long defenderId = inGame.getTurnData().getDefenderId();
        InGamePlayer defender = gameRedisRepository.getInGamePlayer(gameId, defenderId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));

        return defender.isDead() && defender.isLeader() ? END : ATTACK;
    }

    private GameStatus getStatusAfterDoubt(TurnData turnData) {
        DoubtData doubtData = turnData.getDoubtData();
        if (doubtData.isDeadLeader()) {
            return END;
        }
        if (doubtData.isDoubtSuccess()) {
            return ATTACK;
        }
        if (DoubtStatus.ATTACK.equals(doubtData.getDoubtStatus())) {
            return turnData.getDefenderId() == doubtData.getSuspectId() ? ATTACK : DEFENSE;
        }
        return turnData.getAttackerId() == doubtData.getSuspectId() ? ATTACK : EXECUTE;
    }

    private InGame getInGame(long gameId) {
        return gameRedisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
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
