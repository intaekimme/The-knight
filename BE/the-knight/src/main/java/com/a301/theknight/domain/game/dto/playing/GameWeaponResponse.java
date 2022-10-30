package com.a301.theknight.domain.game.dto.playing;

import com.a301.theknight.domain.player.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameWeaponResponse {
    private Team team;
    private GameWeaponData gameWeaponData;
}
