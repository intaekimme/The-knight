package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.game.entity.redis.DefendData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static com.a301.theknight.domain.game.entity.GameStatus.EXECUTE;

@Service
public class DefenseTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;

    public DefenseTimeLimitService(GameRedisRepository redisRepository, RedissonClient redissonClient) {
        super(redisRepository, redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
        TurnData turnData = inGame.getTurnData();
        DefendData defendData = turnData.getDefendData();
        defendData.defendPass();

        redisRepository.saveInGame(gameId, inGame);
    }
}
