package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.entity.Weapon;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
public class AttackData implements Serializable {
    private final Hand attackHand;
    private final Weapon weapon;

    @Builder
    public AttackData(String weapon, String hand) {
        this.attackHand = Hand.valueOf(hand);
        this.weapon = Weapon.valueOf(weapon);
    }
}
