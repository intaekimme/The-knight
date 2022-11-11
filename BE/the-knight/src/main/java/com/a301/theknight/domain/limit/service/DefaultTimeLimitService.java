package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class DefaultTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;

    public DefaultTimeLimitService(GameRedisRepository redisRepository, SendMessageService sendMessageService, RedissonClient redissonClient) {
        super(redisRepository, sendMessageService, redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
        //TODO: GameStatus를 Next로 변경해주는 코드를 여기서 처리.
        // 각 Status 별로 NextStatus를 리턴하는 메소드 작성하기
    }
}
