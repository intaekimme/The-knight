package com.a301.theknight.domain.game.dto.attack.request;

import com.a301.theknight.domain.game.dto.attack.DefendPlayerIdDto;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.Hand;
import com.a301.theknight.global.validation.ValidEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameAttackRequest {
    @NotNull(message = "Defender information is required.")
    private DefendPlayerIdDto defender;

    @NotNull
    @ValidEnum(enumClass = Weapon.class)
    private Weapon weapon;
    @NotNull
    @ValidEnum(enumClass = Hand.class)
    private Hand hand;
}
