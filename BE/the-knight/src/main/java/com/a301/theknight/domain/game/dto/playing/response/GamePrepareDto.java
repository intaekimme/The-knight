package com.a301.theknight.domain.game.dto.playing.response;

import com.a301.theknight.domain.game.dto.playing.GameWeaponData;
import com.a301.theknight.domain.game.dto.playing.response.GameLeaderDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GamePrepareDto {
    private GameWeaponData gameWeaponData;
    private GameLeaderDto gameLeaderDto;
}
