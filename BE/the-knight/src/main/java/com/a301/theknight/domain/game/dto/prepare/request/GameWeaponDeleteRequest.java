package com.a301.theknight.domain.game.dto.prepare.request;

import com.a301.theknight.domain.game.entity.redis.Hand;
import lombok.Data;

@Data
public class GameWeaponDeleteRequest {
    private Hand deleteHand;
}
