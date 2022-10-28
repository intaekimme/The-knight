package com.a301.theknight.domain.game.dto.playing;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GamePrepareDto {
    private GameWeaponDto gameWeaponDto;
    private GameLeaderDto gameLeaderDto;
}
