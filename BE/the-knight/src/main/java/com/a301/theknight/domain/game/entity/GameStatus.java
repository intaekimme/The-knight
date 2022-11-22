package com.a301.theknight.domain.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameStatus {
    //TODO 시연을 위해 강제로 제한시간 늘림.
    WAITING(5_000L),
    PLAYING(5_000L),
    END(5_000L),
    PREPARE(100_000L),
    PREDECESSOR(5_000L),
    ATTACK(60_000L),
    DEFENSE(60_000L),
    ATTACK_DOUBT(60_000L),
    DEFENSE_DOUBT(60_000L),
    DOUBT_RESULT(5_000L),
    EXECUTE(5_000L),

    ;
    private final long limitMilliSeconds;
}

