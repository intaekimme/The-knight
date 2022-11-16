package com.a301.theknight.domain.game.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DefendData {
    private Hand defendHand;
    private int shieldCount;
    private boolean isDefendPass;

    @Builder
    public DefendData(Hand hand, int shieldCount){
        this.defendHand = hand;
        this.shieldCount = shieldCount;
        this.isDefendPass = false;
    }

    public void defendPass(){
        this.isDefendPass = true;
    }
}
