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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static com.a301.theknight.domain.game.entity.GameStatus.*;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameConvertUtil {
    private final GameRedisRepository gameRedisRepository;
    private final RedissonClient redissonClient;
    private final Map<Long, Integer> peopleMap = new ConcurrentHashMap<>();
    private final Map<Long, ConcurrentLinkedQueue<String>> countMap = new HashMap<>();

    @Transactional
    public ConvertResponse convertScreen(long gameId) {
        InGame inGame = getInGame(gameId);
        GameStatus curStatus = getGameStatus(gameId);

        GameStatus nextStatus = getNextStatus(gameId, inGame, curStatus);
        inGame.changeStatus(nextStatus);
        gameRedisRepository.saveInGame(gameId, inGame);

        return new ConvertResponse(curStatus, nextStatus);
    }

    @Transactional
    public boolean requestCounting(long gameId) {
        Integer maxMember = peopleMap.get(gameId);
        ConcurrentLinkedQueue<String> queue = countMap.get(gameId);
        queue.add("count");
        if (queue.size() >= maxMember) {
            try {
                RLock countLock = redissonClient.getLock(generateCountLockKey(gameId));
                if (countLock.tryLock(1, 10, TimeUnit.SECONDS)) {
                    return true;
                }
            } catch (InterruptedException e) {
                log.info("<< Count Lock Error");
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public void initRequestQueue(long gameId, int maxMember) {
        peopleMap.put(gameId, maxMember);
        ConcurrentLinkedQueue<String> queue = countMap.computeIfAbsent(gameId, key -> new ConcurrentLinkedQueue<>());
        queue.clear();
    }

    public void initRequestQueue(long gameId) {
        ConcurrentLinkedQueue<String> queue = countMap.get(gameId);
        queue.clear();

        RLock countLock = redissonClient.getLock(generateCountLockKey(gameId));
        if (countLock != null && countLock.isLocked()) {
            countLock.unlock();
        }
    }

    public void clearData(long gameId) {
        countMap.remove(gameId);
        peopleMap.remove(gameId);
    }

    public GameStatus getGameStatus(long gameId) {
        return getInGame(gameId).getGameStatus();
    }

    public GameStatus getNextStatus(long gameId, InGame inGame, GameStatus gameStatus) {
        TurnData turnData = inGame.getTurnData();

        switch (gameStatus) {
            case PREPARE:
                return PREDECESSOR;
            case PREDECESSOR:
                return ATTACK;
            case ATTACK:
                return getStatusAfterAttack(turnData.getAttackData());
            case ATTACK_DOUBT:
                return getStatusAfterAttackDoubt(turnData.getDoubtData());
            case DEFENSE:
                return getStatusAfterDefense(turnData.getDefenseData());
            case DEFENSE_DOUBT:
                return getStatusAfterDefenseDoubt(turnData.getDoubtData());
            case EXECUTE:
                return getStatusAfterExecute(gameId, turnData);
            case DOUBT_RESULT:
                return getStatusAfterDoubt(turnData);
            case END:
                return null;
        }
        throw new CustomWebSocketException(WRONG_GAME_STATUS);
    }

    private GameStatus getStatusAfterDefenseDoubt(DoubtData doubtData) {
        return doubtData.getSuspectId() > 0 ? DOUBT_RESULT : EXECUTE;
    }

    private GameStatus getStatusAfterDefense(DefendData defendData) {
        return defendData.getDefendHand() != null ? DEFENSE_DOUBT : EXECUTE;
    }

    private GameStatus getStatusAfterAttackDoubt(DoubtData doubtData) {
        return doubtData.getSuspectId() > 0 ? DOUBT_RESULT : DEFENSE;
    }

    private GameStatus getStatusAfterAttack(AttackData attackData) {
        return attackData.getWeapon() == null ? ATTACK : ATTACK_DOUBT;
    }

    private GameStatus getStatusAfterExecute(long gameId, TurnData turnData) {
        long defenderId = turnData.getDefenderId();
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

    private String generateNextKey(long gameId) {
        return "game_next:" + gameId;
    }

    private String generateCountLockKey(long gameId) {
        return "game_count_lock:" + gameId;
    }

}
