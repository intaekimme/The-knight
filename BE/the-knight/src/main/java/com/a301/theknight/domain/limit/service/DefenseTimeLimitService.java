package com.a301.theknight.domain.limit.service;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.DefendData;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.entity.redis.TurnData;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static com.a301.theknight.domain.game.entity.GameStatus.DEFENSE;
import static com.a301.theknight.domain.game.entity.GameStatus.EXECUTE;
import static com.a301.theknight.global.error.errorcode.GamePlayingErrorCode.UNABLE_TO_PASS_DEFENSE;

@Service
public class DefenseTimeLimitService extends TimeLimitServiceTemplate {

    private final GameRedisRepository redisRepository;

    public DefenseTimeLimitService(GameRedisRepository redisRepository, SendMessageService sendMessageService, RedissonClient redissonClient) {
        super(redisRepository, sendMessageService, redissonClient);
        this.redisRepository = redisRepository;
    }

    @Override
    public void runLimitLogic(long gameId, InGame inGame) {
        TurnData turnData = inGame.getTurnData();
        DefendData defendData = turnData.getDefendData();
        defendData.defendPass();

        inGame.changeStatus(EXECUTE);
        redisRepository.saveInGame(gameId, inGame);
    }
}
