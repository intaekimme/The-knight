package com.a301.theknight.domain.game.dto.defense.request;

import com.a301.theknight.domain.game.dto.attack.DefendPlayerDto;
import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.domain.game.entity.redis.Hand;
import com.a301.theknight.global.validation.ValidEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameDefenseRequest {
    @NotNull(message = "Defender information is required.")
    private DefendPlayerDto defender;

    @NotNull
    @ValidEnum(enumClass = Weapon.class)
    private Weapon weapon;
    @NotNull
    @ValidEnum(enumClass = Hand.class)
    private Hand hand;
}
