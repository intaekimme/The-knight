package com.a301.theknight.domain.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameStatus {
    //TODO 시연을 위해 강제로 제한시간 늘림.
    WAITING(5_000L),
    PLAYING(5_000L),
    END(10_000L),
    PREPARE(300_000L),
    PREDECESSOR(10_000L),
    ATTACK(300_000L),
    DEFENSE(300_000L),
    ATTACK_DOUBT(300_000L),
    DEFENSE_DOUBT(300_000L),
    DOUBT_RESULT(10_000L),
    EXECUTE(10_000L),

    ;
    private final long limitMilliSeconds;
}

