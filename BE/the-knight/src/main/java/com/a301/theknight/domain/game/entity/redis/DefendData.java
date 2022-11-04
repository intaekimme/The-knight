package com.a301.theknight.domain.game.entity.redis;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Getter
public class DefendData implements Serializable {
    private final Hand defendHand;
    private final int shieldCount;

    @Builder
    public DefendData(String hand, int shieldCount){
        this.defendHand = Hand.valueOf(hand);
        this.shieldCount = shieldCount;
    }
}
