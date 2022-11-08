package com.a301.theknight.domain.limit.factory;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.limit.service.AttackTimeLimitService;
import com.a301.theknight.domain.limit.service.DefenseTimeLimitService;
import com.a301.theknight.domain.limit.service.DoubtTimeLimitService;
import com.a301.theknight.domain.limit.service.PrepareTimeLimitService;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TimeLimitServiceFactory {

    private final GameRedisRepository redisRepository;
    private final SendMessageService sendMessageService;
    private final RedissonClient redissonClient;
    private final GameRepository gameRepository;

    public TimeLimitServiceTemplate getTimeLimitService(GameStatus gameStatus) {
        if (gameStatus != null) {
            switch (gameStatus) {
                case PREPARE:
                    return new PrepareTimeLimitService(redisRepository, sendMessageService,
                            redissonClient, gameRepository);
                case ATTACK:
                    return new AttackTimeLimitService(redisRepository, sendMessageService, redissonClient);
                case DEFENSE:
                    return new DefenseTimeLimitService(redisRepository, sendMessageService, redissonClient);
                case ATTACK_DOUBT: DEFENSE_DOUBT:
                    return new DoubtTimeLimitService(redisRepository, sendMessageService, redissonClient);
            }
        }
        return null;
    }

}
