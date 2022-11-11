package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.DoubtData;
import com.a301.theknight.domain.game.entity.redis.DoubtStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.InGamePlayer;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static com.a301.theknight.domain.game.entity.GameStatus.*;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.*;

@Service
public class DefaultTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;

    public DefaultTimeLimitService(GameRedisRepository redisRepository, SendMessageService sendMessageService, RedissonClient redissonClient) {
        super(redisRepository, sendMessageService, redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
        GameStatus curStatus = inGame.getGameStatus();
        GameStatus nextStatus = getNextStatus(gameId, inGame, curStatus);

        inGame.changeStatus(nextStatus);
        redisRepository.saveInGame(gameId, inGame);
    }

    private GameStatus getNextStatus(long gameId, InGame inGame, GameStatus gameStatus) {
        switch (gameStatus) {
            case PREDECESSOR:
                return ATTACK;
            case EXECUTE:
                return getStatusAfterExecute(gameId, inGame);
            case DOUBT_RESULT:
                return getStatusAfterDoubt(inGame.getTurnData().getDoubtData());
        }
        throw new CustomWebSocketException(WRONG_GAME_STATUS);
    }

    private GameStatus getStatusAfterExecute(long gameId, InGame inGame) {
        long defenderId = inGame.getTurnData().getDefenderId();
        InGamePlayer defender = redisRepository.getInGamePlayer(gameId, defenderId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_PLAYER_IS_NOT_EXIST));

        return defender.isDead() && defender.isLeader() ? END : ATTACK;
    }

    private GameStatus getStatusAfterDoubt(DoubtData doubtData) {
        if (doubtData.isDeadLeader()) {
            return END;
        }
        if (doubtData.isDoubtResult()) {
            return ATTACK;
        }
        return DoubtStatus.ATTACK.equals(doubtData.getDoubtStatus()) ? DEFENSE : EXECUTE;
    }
}
