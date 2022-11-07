package com.a301.theknight.domain.game.dto.end;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerWeaponDto {
    private long id;
    private String leftWeapon;
    private String rightWeapon;
}
