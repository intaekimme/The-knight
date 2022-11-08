package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class PrepareTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;

    public PrepareTimeLimitService(GameRedisRepository redisRepository, SendMessageService sendMessageService, RedissonClient redissonClient) {
        super(redisRepository, sendMessageService, redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
        //상태 확인하고 랜덤 무기 지정
        //인게임 상태 변경하고 save
    }
}
