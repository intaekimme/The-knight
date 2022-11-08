package com.a301.theknight.domain.game.dto.prepare.request;

import com.a301.theknight.domain.game.entity.Weapon;
import com.a301.theknight.global.validation.ValidEnum;
import lombok.Data;

@Data
public class GameWeaponChoiceRequest {
    @ValidEnum(enumClass = Weapon.class)
    private Weapon weapon;
}
