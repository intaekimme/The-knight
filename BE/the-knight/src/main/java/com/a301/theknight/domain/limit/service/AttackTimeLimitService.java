package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class AttackTimeLimitService extends TimeLimitServiceTemplate {

    public AttackTimeLimitService(GameRedisRepository redisRepository, SendMessageService sendMessageService, RedissonClient redissonClient) {
        super(redisRepository, sendMessageService, redissonClient);
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
    }
}
