package com.a301.theknight.domain.limit.factory;

import com.a301.theknight.domain.game.entity.GameStatus;
import com.a301.theknight.domain.limit.service.DefaultTimeLimitService;
import com.a301.theknight.domain.limit.service.DefenseTimeLimitService;
import com.a301.theknight.domain.limit.service.PrepareTimeLimitService;
import com.a301.theknight.domain.limit.template.TimeLimitServiceTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TimeLimitServiceFactory {
    private final PrepareTimeLimitService prepareTimeLimitService;
    private final DefenseTimeLimitService defenseTimeLimitService;
    private final DefaultTimeLimitService defaultTimeLimitService;

    public TimeLimitServiceTemplate getTimeLimitService(GameStatus gameStatus) {
        switch (gameStatus) {
            case PREPARE:
                return prepareTimeLimitService;
            case DEFENSE:
                return defenseTimeLimitService;
            default:
                return defaultTimeLimitService;
        }
    }

}
