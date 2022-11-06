package com.a301.theknight.domain.player.dto.request;

import com.a301.theknight.domain.player.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerTeamRequest {
    private Team team;
}
