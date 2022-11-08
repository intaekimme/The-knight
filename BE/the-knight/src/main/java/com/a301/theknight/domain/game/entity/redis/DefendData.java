package com.a301.theknight.domain.game.entity.redis;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Getter
public class DefendData implements Serializable {
    private final Hand defendHand;
    private final int shieldCount;
    private boolean isDefendPass;

    @Builder
    public DefendData(String hand, int shieldCount){
        this.defendHand = Hand.valueOf(hand);
        this.shieldCount = shieldCount;
        this.isDefendPass = false;
    }

    public void defendPass(){
        this.isDefendPass = true;
    }
}
