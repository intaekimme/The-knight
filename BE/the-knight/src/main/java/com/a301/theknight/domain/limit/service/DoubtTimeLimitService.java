package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static com.a301.theknight.domain.game.entity.GameStatus.*;

@Service
public class DoubtTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;

    public DoubtTimeLimitService(GameRedisRepository redisRepository, RedissonClient redissonClient) {
        super(redisRepository, redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
        GameStatus curStatus = inGame.getGameStatus();
        GameStatus nextStatus = ATTACK_DOUBT.equals(curStatus) ? DEFENSE : EXECUTE;

        inGame.changeStatus(nextStatus);
        redisRepository.saveInGame(gameId, inGame);
    }
}
