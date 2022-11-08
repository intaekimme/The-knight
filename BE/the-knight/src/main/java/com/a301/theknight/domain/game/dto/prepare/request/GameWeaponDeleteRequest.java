package com.a301.theknight.domain.game.dto.prepare.request;

import com.a301.theknight.domain.game.entity.redis.Hand;
import com.a301.theknight.global.validation.ValidEnum;
import lombok.Data;

@Data
public class GameWeaponDeleteRequest {
    @ValidEnum(enumClass = Hand.class)
    private Hand deleteHand;
}
