package com.a301.theknight.domain.limit.factory;

import com.a301.theknight.domain.common.service.SendMessageService;
import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.game.entity.redis.InGame;
import com.a301.theknight.domain.game.repository.GameRedisRepository;
import com.a301.theknight.domain.game.repository.GameRepository;
import com.a301.theknight.domain.limit.service.AttackTimeLimitService;
import com.a301.theknight.domain.limit.service.DefenseTimeLimitService;
import com.a301.theknight.domain.limit.service.DoubtTimeLimitService;
import com.a301.theknight.domain.limit.service.PrepareTimeLimitService;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import com.a301.theknight.global.error.errorcode.GamePlayingErrorCode;
import com.a301.theknight.global.error.exception.CustomWebSocketException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TimeLimitServiceFactory {
    private final PrepareTimeLimitService prepareTimeLimitService;
    private final AttackTimeLimitService attackTimeLimitService;
    private final DefenseTimeLimitService defenseTimeLimitService;
    private final DoubtTimeLimitService doubtTimeLimitService;
    private final GameRedisRepository redisRepository;

    public TimeLimitServiceTemplate getTimeLimitService(long gameId) {
        InGame inGame = redisRepository.getInGame(gameId)
                .orElseThrow(() -> new CustomWebSocketException(GamePlayingErrorCode.INGAME_IS_NOT_EXIST));

        switch (inGame.getGameStatus()) {
            case PREPARE:
                return prepareTimeLimitService;
            case ATTACK:
                return attackTimeLimitService;
            case DEFENSE:
                return defenseTimeLimitService;
            case ATTACK_DOUBT: case DEFENSE_DOUBT:
                return doubtTimeLimitService;
        }
        return null;
    }

}
