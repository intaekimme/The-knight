package com.a301.theknight.domain.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Weapon {
    SWORD(1), TWIN(2), SHIELD(0), HAND(0), HIDE(0)

    ;
    private final int count;
}
