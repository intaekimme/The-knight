package com.a301.theknight.domain.game.entity.redis;

import com.a301.theknight.domain.game.entity.Weapon;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AttackData {
    private Hand attackHand;
    private Weapon weapon;

    @Builder
    public AttackData(Weapon weapon, Hand hand) {
        this.attackHand = hand;
        this.weapon = weapon;
    }
}
