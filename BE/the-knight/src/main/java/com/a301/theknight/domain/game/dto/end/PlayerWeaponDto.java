package com.a301.theknight.domain.game.dto.end;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerWeaponDto {
    private long memberId;
    private String leftWeapon;
    private String rightWeapon;
}
