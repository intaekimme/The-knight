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

    private final PrepareTimeLimitService prepareTimeLimitService;
    private final AttackTimeLimitService attackTimeLimitService;
    private final DefenseTimeLimitService defenseTimeLimitService;
    private final DoubtTimeLimitService doubtTimeLimitService;

    public TimeLimitServiceTemplate getTimeLimitService(GameStatus gameStatus) {
        if (gameStatus != null) {
            switch (gameStatus) {
                case PREPARE:
                    return prepareTimeLimitService;
                case ATTACK:
                    return attackTimeLimitService;
                case DEFENSE:
                    return defenseTimeLimitService;
                case ATTACK_DOUBT: case DEFENSE_DOUBT:
                    return doubtTimeLimitService;
            }
        }
        return null;
    }

}
