package com.a301.theknight.domain.game.dto.prepare.response;

import com.a301.theknight.domain.game.entity.redis.GameWeaponData;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GamePrepareDto {
    private GameWeaponData gameWeaponData;
    private GameLeaderDto gameLeaderDto;
}
