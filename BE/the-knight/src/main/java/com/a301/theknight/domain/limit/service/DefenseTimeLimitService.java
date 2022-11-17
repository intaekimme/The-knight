package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.game.entity.redis.DefendData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.INGAME_IS_NOT_EXIST;

@Service
public class DefenseTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;

    public DefenseTimeLimitService(GameRedisRepository redisRepository, RedissonClient redissonClient) {
        super(redisRepository, redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void runLimitLogic(long gameId) {
        InGame inGame = getInGame(gameId);
        TurnData turnData = inGame.getTurnData();

        DefendData defendData = turnData.getDefendData();
        defendData.defendPass();

        redisRepository.saveInGame(gameId, inGame);
    }

    private InGame getInGame(long gameId) {
        return redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(INGAME_IS_NOT_EXIST));
    }
}
