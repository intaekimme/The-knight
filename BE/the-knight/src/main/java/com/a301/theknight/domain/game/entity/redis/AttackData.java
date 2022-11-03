package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.entity.Weapon;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class AttackData implements Serializable {
    private Hand attackHand;
    private Weapon weapon;
}
