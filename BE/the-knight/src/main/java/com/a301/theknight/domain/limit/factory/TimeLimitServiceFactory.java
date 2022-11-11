package com.a301.theknight.domain.limit.factory;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.limit.service.*;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TimeLimitServiceFactory {
    private final PrepareTimeLimitService prepareTimeLimitService;
    private final AttackTimeLimitService attackTimeLimitService;
    private final DefenseTimeLimitService defenseTimeLimitService;
    private final DoubtTimeLimitService doubtTimeLimitService;
    private final DefaultTimeLimitService defaultTimeLimitService;

    public TimeLimitServiceTemplate getTimeLimitService(GameStatus gameStatus) {
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
        return defaultTimeLimitService;
    }

}
