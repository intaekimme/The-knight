package com.a301.theknight.domain.game.dto.prepare.response;

import com.a301.theknight.domain.player.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GamePreAttackResponse {
    private Team preAttackTeam;
}
