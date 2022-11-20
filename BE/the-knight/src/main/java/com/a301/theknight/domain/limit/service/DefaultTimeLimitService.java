package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.util.GameLockUtil;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import org.springframework.stereotype.Service;

@Service
public class DefaultTimeLimitService extends TimeLimitServiceTemplate {

    public DefaultTimeLimitService(GameRedisRepository redisRepository, GameLockUtil gameLockUtil) {
        super(redisRepository, gameLockUtil);
    }

    @Override
    public void runLimitLogic(long gameId) {
    }

}
