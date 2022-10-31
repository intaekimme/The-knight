package com.a301.theknight.domain.game.dto.playing;

import com.a301.theknight.domain.game.entity.Weapon;
import lombok.Data;

@Data
public class GameWeaponChoiceRequest {
    private Weapon weapon;
}
