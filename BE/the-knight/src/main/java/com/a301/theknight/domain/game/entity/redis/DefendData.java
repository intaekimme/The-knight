package com.a301.theknight.domain.game.entity.redis;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Getter
public class DefendData implements Serializable {
    private final Hand defendHand;
    private int shieldCount;

    @Builder
    public DefendData(String hand){
        this.defendHand = Hand.valueOf(hand);
    }
}
