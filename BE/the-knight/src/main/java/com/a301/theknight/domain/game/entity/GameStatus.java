package com.a301.theknight.domain.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameStatus {
    WAITING(5L),
    PLAYING(5L),
    END(5L),
    PREPARE(100L),
    PREDECESSOR(5L),
    ATTACK(60L),
    DEFENSE(60L),
    ATTACK_DOUBT(60L),
    DEFENSE_DOUBT(60L),
    DOUBT_RESULT(5L),
    EXECUTE(5L),

    ;
    private final long limitSeconds;
}

