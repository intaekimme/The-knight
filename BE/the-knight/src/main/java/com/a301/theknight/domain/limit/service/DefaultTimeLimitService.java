package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class DefaultTimeLimitService extends TimeLimitServiceTemplate {

    public DefaultTimeLimitService(GameRedisRepository redisRepository, RedissonClient redissonClient) {
        super(redisRepository, redissonClient);
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
    }

}
