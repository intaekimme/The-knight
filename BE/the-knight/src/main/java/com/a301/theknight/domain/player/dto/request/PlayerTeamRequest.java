package com.a301.theknight.domain.player.dto.request;

import com.a301.theknight.domain.player.entity.Team;
import com.a301.theknight.global.validation.ValidEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerTeamRequest {
    @ValidEnum(enumClass = Team.class)
    private Team team;
}
