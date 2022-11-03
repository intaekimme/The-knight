package com.a301.theknight.domain.game.entity.redis;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class DefendData implements Serializable {
    private Hand defendHand;
    private int shieldCount;
}
