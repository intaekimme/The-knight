package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameConvertUtil;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Service
public class DefaultTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;
    private final GameConvertUtil gameConvertUtil;

    public DefaultTimeLimitService(GameRedisRepository redisRepository, RedissonClient redissonClient, GameConvertUtil gameConvertUtil) {
        super(redisRepository, redissonClient);
        this.redisRepository = redisRepository;
        this.gameConvertUtil = gameConvertUtil;
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
        GameStatus nextStatus = gameConvertUtil.getNextStatus(gameId, inGame, inGame.getGameStatus());

        inGame.changeStatus(nextStatus);
        redisRepository.saveInGame(gameId, inGame);
    }

}
