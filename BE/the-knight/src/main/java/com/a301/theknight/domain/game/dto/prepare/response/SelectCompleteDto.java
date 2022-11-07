package com.a301.theknight.domain.game.dto.prepare.response;

import com.a301.theknight.domain.player.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
public class SelectCompleteDto {
    private boolean completed;
    private Team selectTeam;
}
